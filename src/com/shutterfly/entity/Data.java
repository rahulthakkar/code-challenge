package com.shutterfly.entity;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.json.JsonObject;

import com.shutterfly.utility.Constants;

public class Data {

	private Map<String, Customer> customers;
	private Timestamp dataCollectionStart;
	private Timestamp dataCollectionEnd;

	public Data() {
		customers = new HashMap<>();
		
	}
	
	public boolean ingest(JsonObject obj) {
		String eventType = obj.getString(Constants.TYPE_KEY, ""); 
		
		switch(eventType) {
			case Constants.EVENT_TYPE_CUSTOMER:
				return ingestCustomer(obj);
				
			case Constants.EVENT_TYPE_SITE_VISIT:
				return ingestSiteVisit(obj);
				
			case Constants.EVENT_TYPE_IMAGE:
				return ingestImage(obj);
				
			case Constants.EVENT_TYPE_ORDER:
				return ingestOrder(obj);
			default:
				//TODO - LOG
				return false;
		}
	}

	private boolean ingestOrder(JsonObject obj) {
		String orderID = obj.getString(Constants.KEY, "");
		String eventTime = obj.getString(Constants.EVENT_TIME_KEY, "");
		String custID = obj.getString(Constants.CUSTOMER_ID_KEY, "defaultID");
		double amount = Double.parseDouble(obj.getString(Constants.TOTAL_AMOUNT_KEY, "0.0"));
		
		if("".equals(orderID)) {
			return false;
		}
		
		Timestamp timestamp = null;
		if(!"".equals(eventTime)){
			timestamp = Timestamp.valueOf(eventTime);
		}
		
		Order order = new Order(orderID, timestamp, custID, amount);
		Customer customer = getCustomerByID(custID);
		//customer.addOrder(order);
		
		updateTimestamps(timestamp);
		return true;
	}

	private void updateTimestamps(Timestamp timestamp) {
		if(timestamp == null) {
			return;
		}
		if(dataCollectionStart == null || timestamp.before(dataCollectionStart)){
			dataCollectionStart = timestamp;
		}
		if(dataCollectionEnd == null || timestamp.after(dataCollectionEnd)){
			dataCollectionEnd = timestamp;
		}
	}

	private Customer getCustomerByID(String custID) {
		Customer customer = customers.get(custID);
		if(customer==null) {
			customer = new Customer(custID);
			customers.put(custID, customer);
		}
		return customer;
	}

	private boolean ingestImage(JsonObject obj) {
		String imageID = obj.getString(Constants.KEY, "");
		String eventTime = obj.getString(Constants.EVENT_TIME_KEY, "");
		String custID = obj.getString(Constants.CUSTOMER_ID_KEY, "defaultID");
		String cameraMake = obj.getString(Constants.CAMERA_MAKE_KEY, "");
		String cameraModel = obj.getString(Constants.CAMERA_MODEL_KEY, "");
		
		if("".equals(imageID)) {
			return false;
		}
		
		Timestamp timestamp = null;
		if(!"".equals(eventTime)){
			timestamp = Timestamp.valueOf(eventTime);
		}
		
		Image image = new Image(imageID, timestamp, custID, cameraMake, cameraModel);
		Customer customer = getCustomerByID(custID);
		customer.addImage(image);
		
		updateTimestamps(timestamp);
		return true;
	}

	private boolean ingestSiteVisit(JsonObject obj) {
		String pageID = obj.getString(Constants.KEY, "");
		String eventTime = obj.getString(Constants.EVENT_TIME_KEY, "");
		String custID = obj.getString(Constants.CUSTOMER_ID_KEY, "defaultID");
		//TODO - tags
		Map<String, String> tags = new HashMap<>();
		
		if("".equals(pageID)) {
			return false;
		}
		
		Timestamp timestamp = null;
		if(!"".equals(eventTime)){
			timestamp = Timestamp.valueOf(eventTime);
		}
		
		SiteVisit siteVisit = new SiteVisit(pageID, timestamp, custID, tags);
		Customer customer = getCustomerByID(custID);
		customer.addSiteVisit(siteVisit);
		
		updateTimestamps(timestamp);
		return true;
	}

	private boolean ingestCustomer(JsonObject obj) {
		String custID = obj.getString(Constants.KEY, "");
		String eventTime = obj.getString(Constants.EVENT_TIME_KEY, "");
		String lastName = obj.getString(Constants.LAST_NAME_KEY, "");
		String city = obj.getString(Constants.ADR_CITY_KEY, "");
		String state = obj.getString(Constants.ADR_STATE_KEY, "");
		
		
		if("".equals(custID)) {
			return false;
		}
		
		Timestamp timestamp = null;
		if(!"".equals(eventTime)){
			timestamp = Timestamp.valueOf(eventTime);
		}
		
		Customer customer = customers.get(custID);
		if(customer==null){
			customer = new Customer(custID, timestamp, lastName, city, state);
			customers.put(custID, customer);
		} else {
			customer.updateCustomer(timestamp, lastName, city, state);
		}
		
		updateTimestamps(timestamp);
		return true;
	}

	public Map<String, Double> topXSimpleLTVCust() {

		return null;
	}
}
