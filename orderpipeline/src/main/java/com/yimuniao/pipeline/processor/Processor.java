package com.yimuniao.pipeline.processor;

import com.yimuniao.OrderContext;
import com.yimuniao.StepDefEnum;

public interface Processor {

    /**
     * 
     * @param context
     */
    boolean process(OrderContext context);

    StepDefEnum getStep();

    int getProcessedOrderCount();
}
