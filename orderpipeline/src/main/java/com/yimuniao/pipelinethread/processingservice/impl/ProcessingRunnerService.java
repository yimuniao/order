package com.yimuniao.pipelinethread.processingservice.impl;

import com.yimuniao.OrderContext;
import com.yimuniao.StepDefEnum;
import com.yimuniao.pipelinethread.processingservice.AbstractRunnerService;

public class ProcessingRunnerService extends AbstractRunnerService<OrderContext> {

    public ProcessingRunnerService(int threadNum) {
        super(threadNum);
        step = StepDefEnum.PROCESSING;
    }

    @Override
    public boolean process(OrderContext context) {
        // TODO Write a new OrderStepEntity to mysql, it identify
        // that which step this order processed, and starttime,
        // complete time, and whether failed or not.

        // After processed, send the context to next step. if
        // context is failed, send to failureservice, then send to
        // next normal service.
        return true;
    }

}
