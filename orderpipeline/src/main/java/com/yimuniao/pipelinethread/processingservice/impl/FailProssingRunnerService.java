package com.yimuniao.pipelinethread.processingservice.impl;

import com.yimuniao.OrderContext;
import com.yimuniao.StepDefEnum;
import com.yimuniao.pipelinethread.processingservice.AbstractRunnerService;

/**
 * it is a final service, when messages come to the queue of this service, it
 * means the orders failed.
 * 
 * @author fliang
 *
 */
public class FailProssingRunnerService extends AbstractRunnerService<OrderContext> {

    public FailProssingRunnerService(int threadNum) {
        super(threadNum);
        step = StepDefEnum.FAILED;
    }

    @Override
    public boolean process(OrderContext context) {
        /**
         * TODO Write a new OrderStepEntity to mysql, it identify
         * that which step this order processed, and starttime,
         * complete time, and whether failed or not.
         */
        return true;
    }

}
