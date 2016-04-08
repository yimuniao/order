package com.yimuniao.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.yimuniao.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    /**
     * 
     * @param userId
     * @param step
     * @param completeTime
     */
    @Transactional
    @Modifying
    @Query("update OrderEntity a set a.step = ?2, a.completeTime=?3 where a.orderId = ?1")
    void update(Integer userId, int step, Date completeTime);

    /**
     * 
     * @param id
     * @return
     */
    @Modifying
    @Query(value = "delete from OrderEntity where orderId=?1")
    int delete(int id);

    /**
     * 
     * @param id
     * @return
     */
    @Query("select a from OrderEntity a where a.orderId = ?1")
    OrderEntity findById(int id);

    /**
     * 
     * @param id
     * @param userId
     * @return
     */
    @Query("select a from OrderEntity a where a.orderId = ?1 and a.userId = ?2")
    OrderEntity findByIdAndUserId(int id, String userId);

    /**
     * 
     * @param userId
     * @return
     */
    @Query("select a from OrderEntity a where a.userId = ?1")
    OrderEntity findByUserId(String userId);
    
    /**
     * 
     * @param userId
     * @return
     */
    @Query("select a from OrderEntity a where completeTime == null and starttime <= ?1")
    List<OrderEntity> find(Date before);

}
