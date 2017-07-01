package com.shutterfly.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Weeks;

public class Customer extends UpdatableEventEntity {
	
	private String lastName;
	private String city;
	private String state;
	private List<SiteVisit> siteVisits;
	private List<Image> images;
	private Map<String, Order> orders;
	private double totalOrderAmount;
	private double avgAmountPerWeek;

	public Customer(String key, DateTime dateTime, String lastName, String city, String state) {
		this(key, dateTime);
		this.lastName = lastName;
		this.city = city;
		this.state = state;
	}

	// Construct Customer if Order belonging to this customer comes before
	// Customer creation
	public Customer(String key, DateTime dateTime) {
		super(key, dateTime);
		this.siteVisits = new ArrayList<>();
		this.images = new ArrayList<>();
		this.orders = new HashMap<>();
	}

	public void updateCustomer(DateTime createTime, DateTime lastUpdateTime, String lastName, String city,
			String state) {
		setLastName(lastName, lastUpdateTime);
		setCity(city, lastUpdateTime);
		setState(state, lastUpdateTime);
		setTimeStamps(createTime);
		setTimeStamps(lastUpdateTime);
	}

	private void setTimeStamps(DateTime dateTime) {
		setCreateTime(dateTime);
		setLastUpdateTime(dateTime);
	}

	public void addOrder(Order order) {
		if (orders.containsKey(order.getKey())) {
			updateOrder(order);
		} else {
			orders.put(order.getKey(), order);
			totalOrderAmount += order.getAmount();
		}
		setTimeStamps(order.getCreateTime());
		setTimeStamps(order.getLastUpdateTime());
	}

	private boolean updateOrder(Order order) {
		if (order == null || getKey() != order.getCustID() || !orders.containsKey(order.getKey())) {
			return false;
		}
		Order oldOrder = orders.get(order.getKey());
		if(oldOrder.getLastUpdateTime()==null || 
				oldOrder.getLastUpdateTime().isBefore(order.getLastUpdateTime())){
			// Update Total if changed
			totalOrderAmount += (order.getAmount() - oldOrder.getAmount());
			oldOrder.updateOrder(order);
		}
		return true;
	}

	public double getAvgAmountPerWeek(DateTime endTime) {
		if (getCreateTime() == null || endTime == null || getCreateTime().isAfter(endTime)) {
			return 0.0;
		}

		int weeks = Weeks.weeksBetween(getCreateTime(), endTime).getWeeks();
		if (weeks == 0) {
			weeks = 1;
		}
		avgAmountPerWeek = totalOrderAmount / (1.0 * weeks);
		return avgAmountPerWeek;
	}

	// Returns already calculated avg amount per week
	public double getAvgAmountPerWeek() {
		return avgAmountPerWeek;
	}

	private void setLastName(String lastName, DateTime lastUpdateTime) {
		if(lastName==null || lastUpdateTime==null){
			return;
		}
		if (this.lastName == null || getLastUpdateTime() == null
				|| lastUpdateTime.isAfter(getLastUpdateTime())) {
			this.lastName = lastName;
		}
	}

	private void setCity(String city, DateTime lastUpdateTime) {
		if(city==null || lastUpdateTime==null){
			return;
		}
		if (this.city == null || getLastUpdateTime() == null
				|| lastUpdateTime.isAfter(getLastUpdateTime())) {
			this.city = city;
		}
	}

	private void setState(String state, DateTime lastUpdateTime) {
		if(state==null || lastUpdateTime==null){
			return;
		}
		if (this.state == null || getLastUpdateTime() == null
				|| lastUpdateTime.isAfter(getLastUpdateTime())) {
			this.state = state;
		}
	}

	public void addImage(Image image) {
		images.add(image);
		setTimeStamps(image.getCreateTime());
	}

	public void addSiteVisit(SiteVisit siteVisit) {
		siteVisits.add(siteVisit);
		setTimeStamps(siteVisit.getCreateTime());
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
		return "Customer [custID=" + getKey() + ", createTime=" + getCreateTime() + ", lastUpdateTime="
				+ getLastUpdateTime() + ", lastName=" + lastName + ", city=" + city + ", state=" + state
				+ ", siteVisits=" + siteVisits + ", images=" + images + ", orders=" + orders + ", totalOrderAmount="
				+ totalOrderAmount + ", avgAmountPerWeek=" + avgAmountPerWeek + "]";
	}

	/*
	 * Ingests 'this' event in the given data
	 */
	@Override
	public boolean ingest(Data data) {
		Customer customer = data.getCustomerByID(getKey());
		if (customer == null) {
			data.addCustomer(this);
		} else {
			// Update customer if already present
			customer.updateCustomer(getCreateTime(), getLastUpdateTime(), lastName, city, state);
		}

		data.updateTimestamps(getCreateTime());
		data.updateTimestamps(getLastUpdateTime());
		return true;
	}

}
