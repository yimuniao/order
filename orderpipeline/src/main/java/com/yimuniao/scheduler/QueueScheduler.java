package com.yimuniao.scheduler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Such class can make it flexible. it is easy to add some filter one the queue.
 * 
 * @author fliang
 *
 * @param <T>
 */
public class QueueScheduler<T> implements Scheduler<T> {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    // TODO: This blocking queue size limit should come from a configuration,
    // properties file or from zookeeper.
    private BlockingQueue<T> queue = new ArrayBlockingQueue<T>(55);

    @Override
    public synchronized T poll() {
        return queue.poll();
    }

    @Override
    public boolean put(T t) {
        try {
            queue.put(t);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            return false;
        }
        return true;
    }

    @Override
    public int getSize() {
        return queue.size();
    }

    @Override
    public BlockingQueue<T> getQueue() {
        return queue;
    }

}
