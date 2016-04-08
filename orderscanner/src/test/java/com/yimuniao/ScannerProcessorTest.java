package com.yimuniao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.yimuniao.entity.OrderEntity;
import com.yimuniao.scanner.ProducerService;
import com.yimuniao.scanner.ScannerProcessor;
import com.yimuniao.service.OrderService;

public class ScannerProcessorTest {
    @InjectMocks
    private ScannerProcessor scannerProcessor = new ScannerProcessor();

    @Mock
    private OrderService orderService;
    @Mock
    private ProducerService producer;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOrderService() {
        List<OrderEntity> orderList = new ArrayList<OrderEntity>();
        for (int i = 0; i < 100; i++) {
            OrderEntity entity = new OrderEntity();
            entity.setOrderId(123445 + i);
            entity.setDescription("order description *******");
            entity.setUserId("admin_test");
            entity.setStartTime(new Date(System.currentTimeMillis() - 1000 * 70));
            entity.setCompleteTime(null);
            orderList.add(entity);
        }

        Mockito.when(orderService.getTimeoutOrder()).thenReturn(orderList);
        scannerProcessor.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(scannerProcessor.getTimeoutMessageNum(), orderList.size());

    }
}
