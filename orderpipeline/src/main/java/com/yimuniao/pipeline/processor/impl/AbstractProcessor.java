package com.yimuniao.pipeline.processor.impl;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yimuniao.OrderContext;
import com.yimuniao.StepDefEnum;
import com.yimuniao.pipeline.processor.Processor;

public abstract class AbstractProcessor implements Processor {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected AtomicInteger processedOrderCount = new AtomicInteger(0);
    protected StepDefEnum step  = null;
    
    public AbstractProcessor() {
    }

    @Override
    public boolean process(OrderContext context) {
        try {
            // TODO: default process

        } catch (Exception e) {
            logger.warn("procession error", e);
            return false;
        }
        return true;
    }
    
    public int getProcessedOrderCount() {
        return processedOrderCount.get();
    }
}
