package com.yimuniao.pipeline;

import java.util.List;

import com.yimuniao.OrderContext;

public interface Pipeline<T> {

    void processPipeline(OrderContext context);

    Pipeline<T> addProcessor(T t);

    List<T> getProcessorList();
}
