package com.yimuniao.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.yimuniao.OrderContext;
import com.yimuniao.pipelinethread.PipelineMultiThreadsExecutor;
import com.yimuniao.pipelinethread.processingservice.PipelineRunnerService;

/*
 * Periodically collect the status and count pipeline runner service, then send to center control node. 
 * Periodically fetch the mode from the configuration center, maybe Zookeeper, 
 *              and according to the mode flag, we can stop the service or switch it to debug mode.
 *              stop the server, stop the consumer from kalka, and wait for some minutes, let all order entity processed, 
 *              then the application can be killed.  
 *              when switch debug mode, we can swith the log level to lower dynamically, and control the speed of consuming kafka, 
 *              then we can see detail log.
 */
public class MonitorService {
    private PipelineMultiThreadsExecutor pipelineExecutor;
    private ExecutorService executor = Executors.newFixedThreadPool(2);
    private volatile int runningMode;
    private volatile int debugMode;

    public MonitorService(PipelineMultiThreadsExecutor pipelineExecutor) {
        debugMode             = 0;
        runningMode           = 0;
        this.pipelineExecutor = pipelineExecutor;
    }

    public void start() {
        CollectStatusThread collectStatusThread = new CollectStatusThread();
        PollingConfigurationThread pollThread = new PollingConfigurationThread();
        executor.submit(collectStatusThread);
        executor.submit(pollThread);
    }

    class CollectStatusThread implements Runnable {

        private CollectStatusThread() {
        }

        public void run() {
            try {

                while (true) {
                    

                    List<PipelineRunnerService<OrderContext>> runnerServiceList = pipelineExecutor.getRunnerServiceList();
                    List<Integer> valueList = new ArrayList<Integer>();
                    for (PipelineRunnerService<OrderContext> service : runnerServiceList) {
                        valueList.add(service.getProcessedOrderCount());
                    }
                    
                   /**
                    *  send the valueList to control center. and maybe it can write ahead to log first. it is the usually way to write some thing important.
                    *  sleep  sometime, then collect the status and count again.
                    */
                   Thread.sleep(5000);
                }
            } catch (InterruptedException ex) {
            }
        }
    }
    
    
    class PollingConfigurationThread implements Runnable {

        private PollingConfigurationThread() {
        }

        public void run() {
            try {

                while (true) {
                    /*
                     * polling the runningMode and debugMode from configuration center, maybe zookeeper
                     * if zookeeper, we can use curator, it a opensource zookeeper client utils.
                     * runningMode mode will be used by schedulerservice, because schedulerservice contains kafka consumer client.
                     */
                      
                    
                   /*
                    * TODO if debugMode != 0, then dynamically change the log4j debug level. 
                    */
                    
                    
                   /*
                    *  sleep  sometime, then poll the configuration again.
                    */
                   Thread.sleep(5000);
                }
            } catch (InterruptedException ex) {
            }
        }
    }


    public int getRunningMode() {
        return runningMode;
    }

}
