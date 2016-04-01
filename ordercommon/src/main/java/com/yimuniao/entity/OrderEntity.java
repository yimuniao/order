package com.yimuniao.entity;

import java.sql.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "orderdemo")
public class OrderEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, updatable = false, insertable = true)
	private int orderId;

	private String userId;
	@Lob
	private String description;

	private int step;

	private Date startTime;

	private Date completeTime;
	
	@OneToMany(fetch = FetchType. LAZY)
	@JoinColumn(name="id")
	private Set<OrderStepEntity> setDep;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
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

    public Set<OrderStepEntity> getSetDep() {
        return setDep;
    }

    public void setSetDep(Set<OrderStepEntity> setDep) {
        this.setDep = setDep;
    }

}
