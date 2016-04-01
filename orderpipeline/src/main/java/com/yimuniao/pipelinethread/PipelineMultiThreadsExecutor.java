package com.yimuniao.pipelinethread;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yimuniao.OrderContext;
import com.yimuniao.entity.OrderEntity;
import com.yimuniao.pipelinethread.processingservice.PipelineRunnerService;

public class PipelineMultiThreadsExecutor {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private PipelineRunnerService<OrderContext> headRunnerService;

    private PipelineRunnerService<OrderContext> completeRunnerService;

    private PipelineRunnerService<OrderContext> failRunnerService;

    private List<PipelineRunnerService<OrderContext>> runnerServiceList = new LinkedList<PipelineRunnerService<OrderContext>>();

    private BlockingQueue<OrderContext> queueForHeaderRunnerService = null;

    public PipelineMultiThreadsExecutor(BlockingQueue<OrderContext> queue) {
        queueForHeaderRunnerService = queue;
    }

    public PipelineMultiThreadsExecutor addRunnerService(PipelineRunnerService<OrderContext> service) {
        if (headRunnerService == null) {
            headRunnerService = service;
            headRunnerService.setQueue(queueForHeaderRunnerService);
        }
        runnerServiceList.add(service);
        return this;
    }

    private void reContructRunnerServiceList() {
        if (runnerServiceList.size() <= 1) {
            return;
        }

        if (runnerServiceList.contains(completeRunnerService)) {
            runnerServiceList.remove(completeRunnerService);
        }

        if (runnerServiceList.contains(failRunnerService)) {
            runnerServiceList.remove(failRunnerService);
        }

        for (int i = 0; i < runnerServiceList.size() - 1; i++) {
            PipelineRunnerService<OrderContext> pipelineRunnerServiceCurr = runnerServiceList.get(i);
            PipelineRunnerService<OrderContext> pipelineRunnerServiceNext = runnerServiceList.get(i + 1);
            pipelineRunnerServiceCurr.setNextPipelineRunnerService(pipelineRunnerServiceNext);
            pipelineRunnerServiceCurr.addFailRunnerService(failRunnerService);
        }
        PipelineRunnerService<OrderContext> pipelineRunnerService = runnerServiceList.get(runnerServiceList.size()-1);
        pipelineRunnerService.setNextPipelineRunnerService(completeRunnerService);
        runnerServiceList.add(completeRunnerService);
        runnerServiceList.add(failRunnerService);
    }

    public void start() {

        if (failRunnerService == null || completeRunnerService == null) {
            logger.error("Fatal Error: There is no Fail processor service or complete processor service initialization.");
            System.exit(1);
        }

        reContructRunnerServiceList();
        
        for (PipelineRunnerService<OrderContext> service : runnerServiceList) {
            service.start();
        }
        
    }

    public void process(OrderEntity entity, boolean failed) {
        OrderContext context = new OrderContext(entity);
        context.setFailed(failed);
        headRunnerService.addToUnHandledQueue(context);
    }

    public PipelineMultiThreadsExecutor addCompleteRunnerService(PipelineRunnerService<OrderContext> completeProcessingRunnerService) {
        this.completeRunnerService = completeProcessingRunnerService;
        if (!runnerServiceList.contains(completeProcessingRunnerService)) {
            runnerServiceList.add(completeProcessingRunnerService);
        }
        return this;
    }

    public PipelineMultiThreadsExecutor addFailRunnerService(PipelineRunnerService<OrderContext> failProcessingRunnerService) {
        this.failRunnerService = failProcessingRunnerService;
        return this;
    }

}
