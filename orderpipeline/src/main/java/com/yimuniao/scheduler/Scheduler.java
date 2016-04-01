package com.yimuniao.scheduler;

import java.util.concurrent.BlockingQueue;

public interface Scheduler<T> {

    boolean put(T t);

    T poll();

    int getSize();

    BlockingQueue<T> getQueue();

}