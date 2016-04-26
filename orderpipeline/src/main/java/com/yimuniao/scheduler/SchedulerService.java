package com.yimuniao.scheduler;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yimuniao.KafkaProperties;
import com.yimuniao.OrderContext;
import com.yimuniao.entity.OrderEntity;
import com.yimuniao.monitor.MonitorService;
import com.yimuniao.pipeline.Pipeline;
import com.yimuniao.pipeline.PipelineSingleThreadExecutor;
import com.yimuniao.pipeline.impl.PipelineImpl;
import com.yimuniao.pipeline.processor.Processor;
import com.yimuniao.pipelinethread.PipelineMultiThreadsExecutor;
import com.yimuniao.pipelinethread.processingservice.impl.CompleteProcessingRunnerService;
import com.yimuniao.pipelinethread.processingservice.impl.FailProssingRunnerService;
import com.yimuniao.pipelinethread.processingservice.impl.PostProcessingRunnerService;
import com.yimuniao.pipelinethread.processingservice.impl.PreProcessingRunnerService;
import com.yimuniao.pipelinethread.processingservice.impl.ProcessingRunnerService;
import com.yimuniao.pipelinethread.processingservice.impl.SchedulingRunnerService;

public class SchedulerService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private boolean pipelineMultithreadsEnabled = false;
    private int consumerCount = 50;
    private Scheduler<OrderContext> scheduler = new QueueScheduler<OrderContext>();
    /**
     * producerService is for polling the messages from Kafka, then put the
     * messages to a blockingqueue consumerService is for pipeline handling.
     */
    private ExecutorService producerService = Executors.newSingleThreadExecutor();
    private ExecutorService consumerService = Executors.newFixedThreadPool(consumerCount);
    /**
     * multi thread pipleline, every steps have many threads to handle.
     */
    PipelineMultiThreadsExecutor pipelineMultiThreadsExecutor;

    /**
     * single thread pipeline service, it means one thread will do all of the
     * steps, there are many threads to do such job.
     */
    private PipelineSingleThreadExecutor pipelineSingleThreadExecutor;
    
    /**
     *  send status and count to control center, and polling the configuration from center.
     */
    private MonitorService monitorService;

    public void produceToScheduler(OrderEntity entity) throws InterruptedException {
        scheduler.put(new OrderContext(entity));
    }

    public OrderContext consume() throws InterruptedException {
        OrderContext context = scheduler.poll();
        return context;
    }

    class ProducerToPipeline implements Runnable {
        final KafkaConsumer<Integer, OrderEntity> consumerFromKafka;
        private final String topic;

        public ProducerToPipeline(String topic) {
            consumerFromKafka = new KafkaConsumer<Integer, OrderEntity>(createProperties());
            this.topic = topic;
        }

        private Properties createProperties() {
            Properties props = new Properties();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.kafkaServerURL+ ":"+ KafkaProperties.kafkaServerPort);
            props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaProperties.groupId);
            // props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
            // props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
            props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, KafkaProperties.connectionTimeOut);
            return props;
        }

        public void run() {
            while(true) {
                try {
                    consumerFromKafka.subscribe(Collections.singletonList(topic));
                        
                        int runningMode = monitorService.getRunningMode();
                        
                        /*
                         * if runningMode is 0, it means stop the app, then sleep 5 minutes, pipeline have enough time to process all of the order.
                         */
                        if (runningMode == 0){
                            Thread.sleep(300);
                            break;
                        }
                        
                        ConsumerRecords<Integer, OrderEntity> records = consumerFromKafka.poll(1000);
                        for (ConsumerRecord<Integer, OrderEntity> record : records) {
                            logger.debug("Received message: (" + record.key() + ", " + record.value() + ") at offset " + record.offset());
                            
                            produceToScheduler(record.value());
                        }
                        
    
                } 
                catch (InterruptedException ex) {
                }
            }
        }
    }

    class PipelineConsumer implements Runnable {
        PipelineSingleThreadExecutor service;

        private PipelineConsumer(PipelineSingleThreadExecutor service) {
            this.service = service;
        }

        public void run() {
            while (true) {
                try {
                        OrderContext context = consume();
                        Pipeline<Processor> pipeline = service.getPipeline();
                        pipeline.processPipeline(context);
                } 
                catch (InterruptedException ex) {
                }
            }
        }
    }

    private void startStaticPipeline() {
        pipelineSingleThreadExecutor = new PipelineSingleThreadExecutor(new PipelineImpl());
        pipelineSingleThreadExecutor.init();

        ProducerToPipeline producer = new ProducerToPipeline(KafkaProperties.topic);
        producerService.submit(producer);

        for (int i = 0; i < consumerCount; i++) {
            PipelineConsumer consumer = new PipelineConsumer(pipelineSingleThreadExecutor);
            consumerService.submit(consumer);
        }

        int consumerCount = ((ThreadPoolExecutor) consumerService).getActiveCount();
        logger.debug("===consumer thread size: " + consumerCount);
    }

    private void startPipelineThreads() {
        ProducerToPipeline producer = new ProducerToPipeline(KafkaProperties.topic);
        producerService.submit(producer);

        pipelineMultiThreadsExecutor = new PipelineMultiThreadsExecutor.Builder().addRunnerService(new SchedulingRunnerService(11))
                                                        .addRunnerService(new PreProcessingRunnerService(10))
                                                        .addRunnerService(new ProcessingRunnerService(9))
                                                        .addRunnerService(new PostProcessingRunnerService(8))
                                                        .setCompleteRunnerService(new CompleteProcessingRunnerService(1))
                                                        .setFailRunnerService(new FailProssingRunnerService(1))
                                                        .setQueueForHeaderRunnerService(scheduler.getQueue())
                                                        .build();

        pipelineMultiThreadsExecutor.start();
    }
    
    private void startMonitorService()
    {
        monitorService = new MonitorService(pipelineMultiThreadsExecutor);
        monitorService.start();
    }

    public void start() {
        if (pipelineMultithreadsEnabled) {
            startPipelineThreads();
        } else {
            startStaticPipeline();
        }
        
        startMonitorService();
    }

    public int getSize() {
        return scheduler.getSize();
    }

    public boolean isPipelineMultithreadsEnabled() {
        return pipelineMultithreadsEnabled;
    }

    public void setPipelineMultithreadsEnabled(boolean pipelineMultithreadsEnabled) {
        this.pipelineMultithreadsEnabled = pipelineMultithreadsEnabled;
    }

}