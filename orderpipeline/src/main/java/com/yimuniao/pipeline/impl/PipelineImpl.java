package com.yimuniao.pipeline.impl;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yimuniao.OrderContext;
import com.yimuniao.StepDefEnum;
import com.yimuniao.pipeline.Pipeline;
import com.yimuniao.pipeline.processor.Processor;

public class PipelineImpl implements Pipeline<Processor> {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private List<Processor> processorList = new LinkedList<Processor>();

    @Override
    public Pipeline<Processor> addProcessor(Processor t) {
        synchronized (processorList) {
            processorList.add(t);
        }
        return this;
    }

    @Override
    public void processPipeline(OrderContext context) {
        for (Processor processor : processorList) {
            // skipped the failed context when the process is not StepDef.FAILED
            if (context.isFailed()) {
                if (processor.getStep() != StepDefEnum.FAILED) {
                    continue;
                } else {
                    processor.process(context);
                    break;
                }

            } else if (processor.getStep() != StepDefEnum.FAILED && !processor.process(context)) {
                logger.error("Processing context  failure " + context);
                break;
            }

        }

    }

    public List<Processor> getProcessorList() {
        return processorList;
    }

}
