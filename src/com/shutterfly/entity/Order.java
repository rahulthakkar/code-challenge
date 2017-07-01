package com.shutterfly.entity;

import org.joda.time.DateTime;

public class Order {
	private String orderID;
	private DateTime createTime;
	private DateTime lastUpdateTime;
	private String custID;
	private double amount;
	
	public Order(String orderID, DateTime dateTime, String custID, double amount) {
		this.orderID = orderID;
		this.createTime = dateTime;
		this.lastUpdateTime = dateTime;
		this.custID = custID;
		this.amount = amount;
	}
	
	
	public String getOrderID() {
		return orderID;
	}

	public DateTime getCreateTime() {
		return createTime;
	}

	public DateTime getLastUpdateTime() {
		return lastUpdateTime;
	}

	public String getCustID() {
		return custID;
	}

	public double getAmount() {
		return amount;
	}
	
	private void setTimeStamps(DateTime dateTime) {
		if(dateTime!=null){
			if(this.createTime!=null){
				if(dateTime.isBefore(this.createTime)){
					this.createTime = dateTime;
				} else if(dateTime.isAfter(this.lastUpdateTime)){
					this.lastUpdateTime = dateTime;
				}
			} else {
				this.createTime = dateTime;
				this.lastUpdateTime = dateTime;
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
	
	public void updateOrder(Order order) {
		setTimeStamps(order.getCreateTime());
		setCustID(order.getCustID());
		setAmount(order.getAmount());
	}


	@Override
	public String toString() {
		return "Order [orderID=" + orderID + ", createTime=" + createTime + ", lastUpdateTime=" + lastUpdateTime
				+ ", custID=" + custID + ", amount=" + amount + "]";
	}
	
	
}
