package com.shutterfly.entity;

import java.sql.Timestamp;

public class Order {
	private String orderID;
	private Timestamp createTime;
	private Timestamp lastUpdateTime;
	private String custID;
	private double amount;
	
	public Order(String orderID, Timestamp timestamp, String custID, double amount) {
		this.orderID = orderID;
		this.createTime = timestamp;
		this.lastUpdateTime = timestamp;
		this.custID = custID;
		this.amount = amount;
	}
	
	
	public String getOrderID() {
		return orderID;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}

	public String getCustID() {
		return custID;
	}

	public double getAmount() {
		return amount;
	}
	
	private void setTimeStamps(Timestamp timestamp) {
		if(timestamp!=null){
			if(this.createTime!=null){
				if(this.createTime.after(timestamp)){
					this.createTime = timestamp;
				} else if(this.lastUpdateTime.before(timestamp)){
					this.lastUpdateTime = timestamp;
				}
			} else {
				this.createTime = timestamp;
				this.lastUpdateTime = timestamp;
			}
		}
	}
	
	private void setCustID(String custID) {
		if(custID!=null && !custID.trim().isEmpty()) {
			this.custID = custID;
		}
	}
	
	private void setAmount(double amount) {
		if(amount>=0) {
			this.amount = amount;
		}
	}
	
	public void updateOrder(Timestamp timestamp, String custID, double amount) {
		setTimeStamps(timestamp);
		setCustID(custID);
		setAmount(amount);
	}


	@Override
	public String toString() {
		return "Order [orderID=" + orderID + ", createTime=" + createTime + ", lastUpdateTime=" + lastUpdateTime
				+ ", custID=" + custID + ", amount=" + amount + "]";
	}
	
	
}
