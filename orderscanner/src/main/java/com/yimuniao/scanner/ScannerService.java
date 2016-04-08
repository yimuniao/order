package com.yimuniao.scanner;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScannerService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    LeaderLatch latch = null;
    CuratorFramework client = null;
    
    @Autowired
    ScannerProcessor processor;
    /**
     * the process will hold on if it is not master, if the master went down, then all of the slaves will do election to get a master.
     * The only one master will do scanner job.
     * @throws Exception
     */
    public void start() throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder().connectString("192.168.1.2:2181").sessionTimeoutMs(2000)
                .connectionTimeoutMs(10000).retryPolicy(retryPolicy).namespace("text").build();
        client.start();
        latch = new LeaderLatch(client, "/leaderSelector");
        latch.start();
        latch.await();
        logger.info("I am master.");
        
        processor.start();

    }

    public void close() throws Exception {
        if (latch != null) {
            latch.close();
        }
        if (client != null) {
            client.close();
        }
    }

}


