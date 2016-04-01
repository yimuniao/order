package com.yimuniao;

import java.util.LinkedList;
import java.util.List;

import com.yimuniao.entity.OrderEntity;
import com.yimuniao.pipeline.Pipeline;
import com.yimuniao.pipeline.PipelineSingleThreadExecutor;
import com.yimuniao.pipeline.impl.PipelineImpl;
import com.yimuniao.pipeline.processor.Processor;

import junit.framework.TestCase;

public class PipelineSingleThreadExecutorTest extends TestCase {
    List<OrderEntity> entityList = new LinkedList<OrderEntity>();
    PipelineSingleThreadExecutor pipelineSingleThreadExecutor;

    public PipelineSingleThreadExecutorTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        pipelineSingleThreadExecutor = new PipelineSingleThreadExecutor(new PipelineImpl());
        pipelineSingleThreadExecutor.init();

        for (int i = 0; i < 100; i++) {
            OrderEntity entity = new OrderEntity();
            entity.setOrderId(123445 + i);
            entity.setDescription("order description *******");
            entity.setUserId("admin_test");
            entityList.add(entity);
        }

    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testApp() {
        Pipeline<Processor> pipeline = null;
        int i = 0;
        for (OrderEntity entity : entityList) {
            OrderContext context = new OrderContext(entity);
            pipeline = pipelineSingleThreadExecutor.getPipeline();
            i++;
            if ((i % 3) == 0) {
                context.setFailed(true);
            } else {
                context.setFailed(false);
            }
            pipeline.processPipeline(context);
        }

        if (pipeline != null) {
            List<Processor> processorList = pipeline.getProcessorList();
            
            assertEquals(processorList.get(0).getProcessedOrderCount(), 67);
            assertEquals(processorList.get(1).getProcessedOrderCount(), 67);
            assertEquals(processorList.get(2).getProcessedOrderCount(), 67);
            assertEquals(processorList.get(3).getProcessedOrderCount(), 67);
            assertEquals(processorList.get(4).getProcessedOrderCount(), 33);
            assertEquals(processorList.get(5).getProcessedOrderCount(), 67);
        }

    }
}
