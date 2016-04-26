package com.yimuniao.pipelinethread.processingservice;

import java.util.concurrent.BlockingQueue;

public interface PipelineRunnerService<T> {
    boolean addToUnHandledQueue(T context);

    boolean setNextPipelineRunnerService(PipelineRunnerService<T> nextService);

    PipelineRunnerService<T> getNextPipelineRunnerService();

    boolean sendToNextStep(T context);

    boolean setFailRunnerService(PipelineRunnerService<T> failRunnerService);

    void setQueue(BlockingQueue<T> q);

    void start();

    int getProcessedOrderCount();
    
    boolean process(T context);

}
