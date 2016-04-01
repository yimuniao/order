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
    public void start() {
        for (int i = 0; i < this.getProcessorThreadNum(); i++) {
            ProcessorThread processorThread = new ProcessorThread();
            this.getProcessorService().submit(processorThread);
        }
    }

    // @Override
    // public boolean sendToNextStep(OrderContext context) {
    // // TODO Auto-generated method stub
    // if (context.isFailed())
    // {
    // this.failRunnerService.addToUnHandledQueue(context);
    // }
    //
    // else if (this.nextRunnerService != null)
    // {
    // nextRunnerService.addToUnHandledQueue(context);
    // }
    // return true;
    // }

    class ProcessorThread implements Runnable {

        private ProcessorThread() {
        }

        public void run() {
            try {
                while (true) {
                    OrderContext context = getQueue().take();
                    /**
                     * TODO processing the ordercontext here.
                     */
                    processedOrderCount.incrementAndGet();
                    context.setStartTime(new Date(System.currentTimeMillis()));
                    context.setStep(step);
                    // context.setFailed(false);

                    /**
                     * TODO Write a new OrderStepEntity to mysql, it identify
                     * that which step this order processed, and starttime,
                     * complete time, and whether failed or not.
                     */

                    /**
                     * After processed, send the context to next step. if
                     * context is failed, send to failureservice, then send to
                     * next normal service.
                     */
                    sendToNextStep(context);
                }
            } catch (InterruptedException ex) {
            }
        }
    }

}
