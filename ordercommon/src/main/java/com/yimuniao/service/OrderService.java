package com.yimuniao.service;

import java.sql.Date;
import java.util.List;

import com.yimuniao.entity.OrderEntity;

public interface OrderService {
    /**
     * 
     * @return
     */
    List<OrderEntity> get();

    /**
     * 
     * @param userId
     * @return
     */
    List<OrderEntity> get(String userId);

    /**
     * Query the order information
     * @param id
     * @param userId
     * @return
     */
    List<OrderEntity> get(int id, String userId);

    /**
     * 
     * @param order
     * @return
     */
    OrderEntity save(OrderEntity order);

    /**
     * 
     * @param id
     * @return
     */
    boolean delete(int id);

    /**
     * 
     * @param order
     * @return
     */
    OrderEntity update(OrderEntity order);
    
    /**
     * 
     * @param before
     * @return
     */
    List<OrderEntity> getTimeoutOrder(Date before);
    
    /**
     * 
     * @param before
     * @return
     */
    List<OrderEntity> getTimeoutOrder();

}
