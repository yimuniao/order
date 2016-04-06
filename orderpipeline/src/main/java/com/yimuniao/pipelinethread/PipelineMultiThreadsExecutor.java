package com.yimuniao.pipelinethread;

import java.util.ArrayList;
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

    private List<PipelineRunnerService<OrderContext>> runnerServiceList = null;

    private PipelineMultiThreadsExecutor() { }

    private void reContructRunnerServiceList() {
        
        if (failRunnerService == null || completeRunnerService == null) {
            logger.error("Fatal Error: There is no Fail processor service or complete processor service initialization.");
            System.exit(1);
        }
        
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
            pipelineRunnerServiceCurr.setFailRunnerService(failRunnerService);
        }
        
        PipelineRunnerService<OrderContext> pipelineRunnerService = runnerServiceList.get(runnerServiceList.size()-1);
        pipelineRunnerService.setNextPipelineRunnerService(completeRunnerService);
        runnerServiceList.add(completeRunnerService);
        completeRunnerService.setFailRunnerService(failRunnerService);
        runnerServiceList.add(failRunnerService);
    }

    public void start() {

        for (PipelineRunnerService<OrderContext> service : runnerServiceList) {
            service.start();
        }
        
    }

    public void process(OrderEntity entity, boolean failed) {
        OrderContext context = new OrderContext(entity);
        context.setFailed(failed);
        headRunnerService.addToUnHandledQueue(context);
    }

    private void setHeadRunnerService(PipelineRunnerService<OrderContext> headRunnerService) {
        this.headRunnerService = headRunnerService;
    }

    private void setCompleteRunnerService(PipelineRunnerService<OrderContext> completeRunnerService) {
        this.completeRunnerService = completeRunnerService;
    }

    private void setFailRunnerService(PipelineRunnerService<OrderContext> failRunnerService) {
        this.failRunnerService = failRunnerService;
    }

    private void setRunnerServiceList(List<PipelineRunnerService<OrderContext>> runnerServiceList) {
        this.runnerServiceList = runnerServiceList;
    }

    public List<Integer> getStatistic() {
        List<Integer> valueList = new ArrayList<Integer>();
        for (PipelineRunnerService<OrderContext> service : runnerServiceList) {
            valueList.add(service.getProcessedOrderCount());
        }
        
        return valueList;
    }
    
    public static class Builder{
        private PipelineRunnerService<OrderContext> headRunnerService;

        private PipelineRunnerService<OrderContext> completeRunnerService;

        private PipelineRunnerService<OrderContext> failRunnerService;

        private List<PipelineRunnerService<OrderContext>> runnerServiceList = new LinkedList<PipelineRunnerService<OrderContext>>();

        private BlockingQueue<OrderContext> queueForHeaderRunnerService = null;
        
        public Builder setQueueForHeaderRunnerService(BlockingQueue<OrderContext> queueForHeaderRunnerService) {
            this.queueForHeaderRunnerService = queueForHeaderRunnerService;
            return this;
        }

        public Builder setHeadRunnerService(PipelineRunnerService<OrderContext> headRunnerService) {
            this.headRunnerService = headRunnerService;
            return this;
        }

        public Builder setCompleteRunnerService(PipelineRunnerService<OrderContext> completeRunnerService) {
            this.completeRunnerService = completeRunnerService;
            return this;
        }

        public Builder setFailRunnerService(PipelineRunnerService<OrderContext> failRunnerService) {
            this.failRunnerService = failRunnerService;
            return this;
        }
        
        public Builder addRunnerService(PipelineRunnerService<OrderContext> service) {
            if (headRunnerService == null) {
                headRunnerService = service;
                headRunnerService.setQueue(queueForHeaderRunnerService);
            }
            runnerServiceList.add(service);
            return this;
        }
        
        public PipelineMultiThreadsExecutor build() {
            PipelineMultiThreadsExecutor pipelineExecutor = new PipelineMultiThreadsExecutor(); 
            pipelineExecutor.setHeadRunnerService(headRunnerService);
            pipelineExecutor.setCompleteRunnerService(completeRunnerService);
            pipelineExecutor.setFailRunnerService(failRunnerService);
            pipelineExecutor.setRunnerServiceList(runnerServiceList);
            pipelineExecutor.reContructRunnerServiceList();
            return pipelineExecutor;
        }  
        
    }

}
