package com.shutterfly.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Customer {
	private String custID;
	private Timestamp createTime;
	private Timestamp lastUpdateTime;
	private String lastName;
	private String city;
	private String state;
	private List<SiteVisit> siteVisits;
	private List<Image> images;
	private Map<String, Order> orders;
	private double totalOrderAmount;
	
	//TODO
	public boolean updateOrder(Order order) {
		if(order==null || custID!=order.getCustID() || !orders.containsKey(order.getOrderID())) {
			return false;
		}
		totalOrderAmount += 0; 
		return true;
	}
	
	
	public Customer(String custID, Timestamp timestamp, String lastName, String city, String state) {
		this.custID = custID;
		this.createTime = timestamp;
		this.lastUpdateTime = timestamp;
		this.lastName = lastName;
		this.city = city;
		this.state = state;
		this.siteVisits = new ArrayList<>();
		this.images = new ArrayList<>();
		this.orders = new HashMap<>();
	}


	public void updateCustomer(Timestamp timestamp, String lastName, String city, String state) {
		setTimeStamps(timestamp);
		setLastName(lastName);
		setCity(city);
		setState(state);
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
	
	private void setLastName(String lastName) {
		if(lastName!=null && !lastName.trim().isEmpty()) {
			this.lastName = lastName;
		}
	}
	
	private void setCity(String city) {
		if(city!=null && !city.trim().isEmpty()) {
			this.city = city;
		}
	}
	
	private void setState(String state) {
		if(state!=null && !state.trim().isEmpty()) {
			this.state = state;
		}
	}
	
}
