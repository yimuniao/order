package com.yimuniao.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@IdClass(OrderStepPK.class)
@Table(name = "orderstep")
public class OrderStepEntity {

	@Id
	@Column(name = "id", nullable = false, updatable = false, insertable = false)
	private int orderId;
	@Id
	private String step;

	private Date startTime;

	private Date completeTime;
	
	@ManyToOne()
	@JoinColumn(name = "id")
	private OrderEntity orderEntity = new OrderEntity();

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}


	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

    public OrderEntity getOrderEntity() {
        return orderEntity;
    }

    public void setOrderEntity(OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
    }

}
