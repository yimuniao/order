package com.yimuniao.pipeline.processor.impl;

import java.sql.Date;

import com.yimuniao.OrderContext;
import com.yimuniao.StepDefEnum;
import com.yimuniao.entity.OrderStepEntity;

public class PostProcessingProcessor extends AbstractProcessor {
    public PostProcessingProcessor() {
        step = StepDefEnum.POSTPROCESSING;
    }

    @Override
    public boolean process(OrderContext context) {
        try {
            if (context.isFailed())
            {
                return true;
            }
            // PostProcessing step
            processedOrderCount.incrementAndGet();
            OrderStepEntity stepEntity = new OrderStepEntity();
            stepEntity.setStartTime(new Date(System.currentTimeMillis()));

            // TODO Processing

            context.setStep(step);

            stepEntity.setCompleteTime(new Date(System.currentTimeMillis()));
            // TODO push stepEntity into database

        } catch (Exception e) {
            logger.warn("procession error", e);
            return false;
        }
        return true;
    }

    public StepDefEnum getStep() {
        return step;
    }
}
