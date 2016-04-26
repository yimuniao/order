package com.yimuniao;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import com.yimuniao.entity.OrderEntity;
import com.yimuniao.pipelinethread.PipelineMultiThreadsExecutor;
import com.yimuniao.pipelinethread.processingservice.impl.CompleteProcessingRunnerService;
import com.yimuniao.pipelinethread.processingservice.impl.FailProssingRunnerService;
import com.yimuniao.pipelinethread.processingservice.impl.PostProcessingRunnerService;
import com.yimuniao.pipelinethread.processingservice.impl.PreProcessingRunnerService;
import com.yimuniao.pipelinethread.processingservice.impl.ProcessingRunnerService;
import com.yimuniao.pipelinethread.processingservice.impl.SchedulingRunnerService;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class PipelineMultiThreadsExecutorTest extends TestCase {
    PipelineMultiThreadsExecutor pipelineExecutor = null;
    
    List<OrderEntity>  entityList = new LinkedList<OrderEntity>();

    /**
     * Create the test case
     *
     * @param testName
     *            name of the test case
     */
    public PipelineMultiThreadsExecutorTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        // TODO Auto-generated method stub
        super.setUp();
//        pipelineExecutor = new PipelineMultiThreadsExecutor(new ArrayBlockingQueue<OrderContext>(50));
        
        for(int i = 0; i<100; i++)
        {
            OrderEntity entity = new OrderEntity();
            entity.setOrderId(123445+i);
            entity.setDescription("order description *******");
            entity.setUserId("admin_test");
            entityList.add(entity);
        }

 
        
    }

    @Override
    protected void tearDown() throws Exception {
        // TODO Auto-generated method stub
        super.tearDown();
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(PipelineMultiThreadsExecutorTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {

        SchedulingRunnerService schedulingService = new SchedulingRunnerService(1);
        PreProcessingRunnerService preProcessingService = new PreProcessingRunnerService(11);
        ProcessingRunnerService processingService = new ProcessingRunnerService(12);
        PostProcessingRunnerService postProcessingService = new PostProcessingRunnerService(1);
        CompleteProcessingRunnerService completeProcessingRunnerService = new CompleteProcessingRunnerService(1);
        FailProssingRunnerService failProcessingRunnerService = new FailProssingRunnerService(1);
        pipelineExecutor = new PipelineMultiThreadsExecutor.Builder().addRunnerService(schedulingService)
                                                                     .addRunnerService(preProcessingService)
                                                                     .addRunnerService(processingService)
                                                                     .addRunnerService(postProcessingService)
                                                                     .setCompleteRunnerService(completeProcessingRunnerService)
                                                                     .setFailRunnerService(failProcessingRunnerService)
                                                                     .setQueueForHeaderRunnerService(new ArrayBlockingQueue<OrderContext>(50))
                                                                     .build();

        pipelineExecutor.start();
        int i = 0;
        for(OrderEntity entity : entityList) {
            i++;
            if((i % 3) == 0) {
                pipelineExecutor.process(entity, true);
            }
            else{
                pipelineExecutor.process(entity, false);
            }
            
        }
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertTrue(true);
        
        assertEquals(schedulingService.getProcessedOrderCount(), 100);
        assertEquals(preProcessingService.getProcessedOrderCount(), 67);
        assertEquals(processingService.getProcessedOrderCount(), 67);
        assertEquals(postProcessingService.getProcessedOrderCount(), 67);
        assertEquals(completeProcessingRunnerService.getProcessedOrderCount(), 67);
        assertEquals(failProcessingRunnerService.getProcessedOrderCount(), 33);
        
    }
}
