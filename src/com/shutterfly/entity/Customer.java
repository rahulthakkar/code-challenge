package com.shutterfly.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Weeks;

public class Customer {
	private String custID;
	private DateTime createTime;
	private DateTime lastUpdateTime;
	private String lastName;
	private String city;
	private String state;
	private List<SiteVisit> siteVisits;
	private List<Image> images;
	private Map<String, Order> orders;
	private double totalOrderAmount;
	private double avgAmountPerWeek;
	
	public Customer(String custID, DateTime dateTime, String lastName, String city, String state) {
		this(custID);
		this.createTime = dateTime;
		this.lastUpdateTime = dateTime;
		this.lastName = lastName;
		this.city = city;
		this.state = state;
	}

	public Customer(String custID) {
		this.custID = custID;
		this.siteVisits = new ArrayList<>();
		this.images = new ArrayList<>();
		this.orders = new HashMap<>();
	}

	public void updateCustomer(DateTime dateTime, String lastName, String city, String state) {
		setTimeStamps(dateTime);
		setLastName(lastName);
		setCity(city);
		setState(state);
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
	
	public void addOrder(Order order) {
		if(orders.containsKey(order.getOrderID())) {
			updateOrder(order);
		} else {
			orders.put(order.getOrderID(), order);
			totalOrderAmount += order.getAmount();
		}
		setTimeStamps(order.getCreateTime());
		setTimeStamps(order.getLastUpdateTime());
	}

	private boolean updateOrder(Order order) {
		if(order==null || custID!=order.getCustID() || !orders.containsKey(order.getOrderID())) {
			return false;
		}
		Order oldOrder = orders.get(order.getOrderID());
		totalOrderAmount += (order.getAmount() - oldOrder.getAmount());
		oldOrder.updateOrder(order);
		return true;
	}
	
	public double getAvgAmountPerWeek(DateTime endTime) {
		if(createTime==null || endTime==null || createTime.isAfter(endTime)){
			return 0.0;
		}
		
		int weeks = Weeks.weeksBetween(createTime, endTime).getWeeks();
		if(weeks==0){
			weeks=1;
		}
		avgAmountPerWeek =  totalOrderAmount/(1.0*weeks);
		return avgAmountPerWeek;
	}
	
	public double getAvgAmountPerWeek() {
		return avgAmountPerWeek;
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


	public void addImage(Image image) {
		images.add(image);
		setTimeStamps(image.getDateTime());
	}

	public void addSiteVisit(SiteVisit siteVisit) {
		siteVisits.add(siteVisit);
		setTimeStamps(siteVisit.getDateTime());
	}

	public String getCustID() {
		return custID;
	}


	public DateTime getCreateTime() {
		return createTime;
	}


	public DateTime getLastUpdateTime() {
		return lastUpdateTime;
	}


	public String getLastName() {
		return lastName;
	}


	public String getCity() {
		return city;
	}


	public String getState() {
		return state;
	}


	public List<SiteVisit> getSiteVisits() {
		return siteVisits;
	}


	public List<Image> getImages() {
		return images;
	}


	public Map<String, Order> getOrders() {
		return orders;
	}


	public double getTotalOrderAmount() {
		return totalOrderAmount;
	}

	@Override
	public String toString() {
		return "Customer [custID=" + custID + ", createTime=" + createTime + ", lastUpdateTime=" + lastUpdateTime
				+ ", lastName=" + lastName + ", city=" + city + ", state=" + state + ", siteVisits=" + siteVisits
				+ ", images=" + images + ", orders=" + orders + ", totalOrderAmount=" + totalOrderAmount + "]";
	}

}
