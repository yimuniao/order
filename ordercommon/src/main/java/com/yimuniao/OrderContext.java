package com.yimuniao;

import java.util.Date;

import com.yimuniao.entity.OrderEntity;

public class OrderContext {
	OrderEntity order;
	
	//During the processing, the orderId, description and startTime are supposed to No modifying.
	//private String orderId;
	//private String description;
	//private Date startTime;
	private StepDefEnum step;
	private Date startTime = null;
	private Date completeTime = null;
	
	private boolean failed = false;
	
	public OrderContext(OrderEntity order) {
		this.order = order;
	}

	public StepDefEnum getStep() {
		return step;
	}

	public void setStep(StepDefEnum step) {
		this.step = step;
	}

	public Date getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	};
	
	@Override
	public String toString() {
		return "OrderContext [order=" + order + ", step=" + step + ", completeTime=" + completeTime + "]";
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public boolean isFailed() {
		return failed;
	}

	public void setFailed(boolean failed) {
		this.failed = failed;
	}
	
	public void reset()
	{
		startTime=null;
		completeTime = null;
		this.step = null;
		failed = false;
	}
}
