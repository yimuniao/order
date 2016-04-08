package com.yimuniao.service.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yimuniao.Constants;
import com.yimuniao.entity.OrderEntity;
import com.yimuniao.repository.OrderRepository;
import com.yimuniao.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<OrderEntity> get() {
        List<OrderEntity> list = new ArrayList<OrderEntity>();
        List<OrderEntity> findAll = orderRepository.findAll();
        if (findAll.isEmpty()) {
            return list;
        }
        return findAll;
    }

    @Override
    public OrderEntity save(OrderEntity order) {
        OrderEntity saveAndFlush = orderRepository.save(order);
        return saveAndFlush;
    }

    @Override
    public boolean delete(int id) {
        OrderEntity order = new OrderEntity();
        order.setOrderId(id);
        orderRepository.delete(order);
        return true;
    }

    @Override
    public OrderEntity update(OrderEntity order) {
        orderRepository.update(order.getOrderId(), order.getStep(), order.getCompleteTime());
        OrderEntity findById = orderRepository.findById(order.getOrderId());
        return findById;
    }

    @Override
    public List<OrderEntity> get(String userId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<OrderEntity> get(int id, String userId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<OrderEntity> getTimeoutOrder(Date before) {
        
        List<OrderEntity> list = new ArrayList<OrderEntity>();
        List<OrderEntity> findAll = orderRepository.findAll();
        if (findAll.isEmpty()) {
            return list;
        }
        
        return list;
    }

    @Override
    public List<OrderEntity> getTimeoutOrder() {
        return getTimeoutOrder(new Date(System.currentTimeMillis() - Constants.TIMEOUTVALUE));
    }


}