package com.yimuniao.pipelinethread.processingservice;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.yimuniao.OrderContext;
import com.yimuniao.StepDefEnum;

public abstract class AbstractRunnerService<T extends OrderContext> implements PipelineRunnerService<T> {
    private BlockingQueue<T> queue = new LinkedBlockingQueue<T>();
    private PipelineRunnerService<T> nextRunnerService;
    private PipelineRunnerService<T> failRunnerService;

    private int processorThreadNum = 1;
    private ExecutorService processorService;
    protected AtomicInteger processedOrderCount = new AtomicInteger(0);
    protected StepDefEnum step;
    protected AbstractRunnerService(int threadNum) {
        if (threadNum > 0) {
            processorThreadNum = threadNum;
        }
        processorService = Executors.newFixedThreadPool(processorThreadNum);
    }

    @Override
    public boolean addToUnHandledQueue(T t) {
        // TODO Auto-generated method stub
        if (queue != null) {
            try {
                queue.put(t);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean setNextPipelineRunnerService(PipelineRunnerService<T> nextService) {
        nextRunnerService = nextService;
        return true;
    }

    @Override
    public PipelineRunnerService<T> getNextPipelineRunnerService() {
        return nextRunnerService;
    }

    @Override
    public boolean setFailRunnerService(PipelineRunnerService<T> failRunnerService) {
        this.failRunnerService = failRunnerService;
        return true;
    }

    @Override
    public boolean sendToNextStep(T context) {
        if (context.isFailed()) {
            this.failRunnerService.addToUnHandledQueue(context);
        }

        else if (this.nextRunnerService != null) {
            nextRunnerService.addToUnHandledQueue(context);
        }
        return true;
    }

    public BlockingQueue<T> getQueue() {
        return queue;
    }

    public void setQueue(BlockingQueue<T> queue) {
        if (queue != null) {
            this.queue = queue;
        }
    }

    public PipelineRunnerService<T> getNextRunnerService() {
        return nextRunnerService;
    }

    public void setNextRunnerService(PipelineRunnerService<T> nextRunnerService) {
        this.nextRunnerService = nextRunnerService;
    }

    public PipelineRunnerService<T> getFailRunnerService() {
        return failRunnerService;
    }

    public ExecutorService getProcessorService() {
        return processorService;
    }

    public void setProcessorService(ExecutorService processorService) {
        this.processorService = processorService;
    }

    public int getProcessorThreadNum() {
        return processorThreadNum;
    }

    public void setProcessorThreadNum(int processorThreadNum) {
        this.processorThreadNum = processorThreadNum;
    }
    
    public int getProcessedOrderCount() {
        return processedOrderCount.get();
    }
}
