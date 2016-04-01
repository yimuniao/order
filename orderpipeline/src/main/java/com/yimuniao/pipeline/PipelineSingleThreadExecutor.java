package com.yimuniao.pipeline;

import com.yimuniao.pipeline.processor.Processor;
import com.yimuniao.pipeline.processor.impl.CompleteProcessingProcessor;
import com.yimuniao.pipeline.processor.impl.FailProcessingProcessor;
import com.yimuniao.pipeline.processor.impl.PostProcessingProcessor;
import com.yimuniao.pipeline.processor.impl.PreProcessingProcessor;
import com.yimuniao.pipeline.processor.impl.ProcessingProcessor;
import com.yimuniao.pipeline.processor.impl.SchedulingProcessor;

public class PipelineSingleThreadExecutor {
    private Pipeline<Processor> pipeline = null;

    public PipelineSingleThreadExecutor(Pipeline<Processor> pipeline) {
        this.pipeline = pipeline;
    }

    public void init() {
        pipeline.addProcessor(new SchedulingProcessor())
                .addProcessor(new PreProcessingProcessor())
                .addProcessor(new ProcessingProcessor())
                .addProcessor(new PostProcessingProcessor())
                .addProcessor(new FailProcessingProcessor())
                .addProcessor(new CompleteProcessingProcessor());
    }

    public Pipeline<Processor> getPipeline() {
        return pipeline;
    }

    public void setPipeline(Pipeline<Processor> pipeline) {
        this.pipeline = pipeline;
    }

}
