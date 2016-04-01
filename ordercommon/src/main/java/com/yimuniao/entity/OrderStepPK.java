package com.yimuniao.entity;

import java.io.Serializable;

public class OrderStepPK implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int orderId;
	private String step;
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
}