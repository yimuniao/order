
package com.yimuniao.scanner;

import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Service;

import com.yimuniao.KafkaProperties;
import com.yimuniao.entity.OrderEntity;

/**
 * get orderentity from queue, then send to kafka server.
 * @author fliang
 *
 */
@Service
public class ProducerService {
    private BlockingQueue<OrderEntity> queue = new LinkedBlockingQueue<OrderEntity>();
    private AtomicInteger messageNum  = new AtomicInteger(0);

    public ProducerService() {
        new Thread(new Producer(KafkaProperties.topic, true)).start();
    }

    public boolean put(OrderEntity entity) {
        try {
            queue.put(entity);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    class Producer implements Runnable {
        private final KafkaProducer<Integer, OrderEntity> producer;
        private final String topic;
        private final Boolean isAsync;

        public Producer(String topic, Boolean isAsync) {
            Properties props = new Properties();
            props.put("bootstrap.servers", KafkaProperties.kafkaServerURL + ":" + KafkaProperties.kafkaServerPort);
            props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
            props.put("client.id", KafkaProperties.clientId);
            producer = new KafkaProducer<Integer, OrderEntity>(props);
            this.topic = topic;
            this.isAsync = isAsync;
        }

        public void run() {

            try {
                messageNum.incrementAndGet();

                while (true) {
                    OrderEntity order = queue.take();
                    if (isAsync) {
                        producer.send(new ProducerRecord<Integer, OrderEntity>(topic, order),
                                new DemoCallBack(System.currentTimeMillis(), messageNum.get(), order));
                    } else {
                        ProducerRecord<Integer, OrderEntity> data = new ProducerRecord<Integer, OrderEntity>(topic,
                                order);
                        producer.send(data);
                    }
                    messageNum.incrementAndGet();
                }
            } catch (Exception e) {

            }
        }
    }

    class DemoCallBack implements Callback {

        private long startTime;
        private int key;
        private OrderEntity message;

        public DemoCallBack(long startTime, int key, OrderEntity message) {
            this.startTime = startTime;
            this.key = key;
            this.message = message;
        }

        public void onCompletion(RecordMetadata metadata, Exception exception) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (metadata != null) {
                System.out.println("message(" + key + ", " + message + ") sent to partition(" + metadata.partition()
                        + "), " + "offset(" + metadata.offset() + ") in " + elapsedTime + " ms");
            } else {
                exception.printStackTrace();
            }
        }

    }

    
    public int getMessageNo() {
        return messageNum.get();
    }
}
