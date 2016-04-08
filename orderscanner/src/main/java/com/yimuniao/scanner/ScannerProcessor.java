package com.yimuniao.scanner;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yimuniao.entity.OrderEntity;
import com.yimuniao.service.OrderService;
/**
 * 
 * @author fliang
 *
 */
@Component
public class ScannerProcessor {
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProducerService producer;
    
  
    private AtomicInteger timeoutMessageNum  = new AtomicInteger(0);
    
    public ScannerProcessor() {
    }

    public void start() {
        ScannerDatabaseThread scannerThread = new ScannerDatabaseThread();
        executor.submit(scannerThread);
    }

    class ScannerDatabaseThread implements Runnable {

        private ScannerDatabaseThread() {
        }

        public void run() {
            try {

                while (true) {
                    /**
                     * TODO scan the database, to get the timeout order, then send the order to kafka agian.
                     * 
                     * if one order started before TIMEOUTVALUE ago and have not complete, we can consider it is timeout.
                     */
                    List<OrderEntity> timeoutOrder = orderService.getTimeoutOrder();
                    for(OrderEntity entity : timeoutOrder){
                        timeoutMessageNum.incrementAndGet();
                        producer.put(entity);
                    }
                    
                   Thread.sleep(5000);
                }
            } catch (InterruptedException ex) {
            }
        }
    }

    public int getTimeoutMessageNum() {
        return timeoutMessageNum.get();
    }

}
