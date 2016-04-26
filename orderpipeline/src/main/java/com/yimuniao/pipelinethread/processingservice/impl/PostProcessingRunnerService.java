package com.yimuniao.pipelinethread.processingservice.impl;

import java.sql.Date;

import com.yimuniao.OrderContext;
import com.yimuniao.StepDefEnum;
import com.yimuniao.pipelinethread.processingservice.AbstractRunnerService;

public class PostProcessingRunnerService extends AbstractRunnerService<OrderContext> {

    public PostProcessingRunnerService(int threadNum) {
        super(threadNum);
        step = StepDefEnum.POSTPROCESSING;
    }

    @Override
    public boolean process(OrderContext context) {
        // TODO Auto-generated method stub
        return false;
    }

}
