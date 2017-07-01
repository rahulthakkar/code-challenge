package com.shutterfly.entity;

import org.joda.time.DateTime;

public class Order extends UpdatableEventEntity{
	private String custID;
	private double amount;
	
	public Order(String key, DateTime dateTime, String custID, double amount) {
		super(key, dateTime);
		this.custID = custID;
		this.amount = amount;
	}
	
	public String getCustID() {
		return custID;
	}

	public double getAmount() {
		return amount;
	}
	
	private void setTimeStamps(DateTime dateTime) {
		setCreateTime(dateTime);
		setLastUpdateTime(dateTime);
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
		return "Order [orderID=" + getKey() + ", createTime=" + getCreateTime() + ", lastUpdateTime=" + getLastUpdateTime()
				+ ", custID=" + custID + ", amount=" + amount + "]";
	}


	/*
	 * Ingests 'this' event in the given data
	 */
	@Override
	public boolean ingest(Data data) {
		Customer customer = data.getCustomerByID(custID, getCreateTime());
		customer.addOrder(this);
		data.updateTimestamps(getCreateTime());
		data.updateTimestamps(getLastUpdateTime());
		return true;
	}
	
	
}
