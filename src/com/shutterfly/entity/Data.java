package com.shutterfly.entity;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import com.shutterfly.utility.Constants;

public class Data {

	private Map<String, Customer> customers;
	private DateTime dataCollectionStart;
	private DateTime dataCollectionEnd;
	private ArrayDeque<Customer> topx;
	private DateTime lastCalculate;

	public Data() {
		customers = new HashMap<>();
		topx = new ArrayDeque<>();
	}

	public boolean ingest(JsonObject obj) {
		String eventType = obj.getString(Constants.TYPE_KEY, "");

		switch (eventType) {
		case Constants.EVENT_TYPE_CUSTOMER:
			return ingestCustomer(obj);

		case Constants.EVENT_TYPE_SITE_VISIT:
			return ingestSiteVisit(obj);

		case Constants.EVENT_TYPE_IMAGE:
			return ingestImage(obj);

		case Constants.EVENT_TYPE_ORDER:
			return ingestOrder(obj);
		default:
			// TODO - LOG
			return false;
		}
	}

	private boolean ingestOrder(JsonObject obj) {
		String orderID = obj.getString(Constants.KEY, "");
		String eventTime = obj.getString(Constants.EVENT_TIME_KEY, "");
		String custID = obj.getString(Constants.CUSTOMER_ID_KEY, "defaultID");
		
		// Dropping anything but digits and . with assumption 
		// - there will be only one . 
		// - order amount is not negative 
		// - all the orders are in same currency
		double amount = Double.parseDouble(obj.getString(Constants.TOTAL_AMOUNT_KEY, "0.0").replaceAll("[^\\d.]", ""));
		
		if ("".equals(orderID)) {
			return false;
		}

		DateTime dateTime = null;
		if (!"".equals(eventTime)) {
			dateTime = ISODateTimeFormat.dateTime().withZoneUTC().parseDateTime(eventTime);
		}

		Order order = new Order(orderID, dateTime, custID, amount);
		Customer customer = getCustomerByID(custID);
		customer.addOrder(order);

		updateTimestamps(dateTime);
		return true;
	}

	private void updateTimestamps(DateTime dateTime) {
		if (dateTime == null) {
			return;
		}
		if (dataCollectionStart == null || dateTime.isBefore(dataCollectionStart)) {
			dataCollectionStart = dateTime;
		}
		if (dataCollectionEnd == null || dateTime.isAfter(dataCollectionEnd)) {
			dataCollectionEnd = dateTime;
		}
	}

	private Customer getCustomerByID(String custID) {
		Customer customer = customers.get(custID);
		if (customer == null) {
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

		if ("".equals(imageID)) {
			return false;
		}

		DateTime dateTime = null;
		if (!"".equals(eventTime)) {
			dateTime = ISODateTimeFormat.dateTime().withZoneUTC().parseDateTime(eventTime);
		}

		Image image = new Image(imageID, dateTime, custID, cameraMake, cameraModel);
		Customer customer = getCustomerByID(custID);
		customer.addImage(image);

		updateTimestamps(dateTime);
		return true;
	}

	private boolean ingestSiteVisit(JsonObject obj) {
		String pageID = obj.getString(Constants.KEY, "");
		String eventTime = obj.getString(Constants.EVENT_TIME_KEY, "");
		String custID = obj.getString(Constants.CUSTOMER_ID_KEY, "defaultID");
		JsonArray tagsArray = obj.getJsonArray(Constants.TAGS_KEY);
		
		// TODO - tags
		Map<String, String> tags = new HashMap<>();
		
		for(JsonValue value: tagsArray) {
			JsonObject object = (JsonObject) value;
			Set<String> keySet = object.keySet();
			for(String key : keySet) {
			tags.put(key, object.getString(key, ""));
			}
		}
		
		if ("".equals(pageID)) {
			return false;
		}

		DateTime dateTime = null;
		if (!"".equals(eventTime)) {
			dateTime = ISODateTimeFormat.dateTime().withZoneUTC().parseDateTime(eventTime);
		}

		SiteVisit siteVisit = new SiteVisit(pageID, dateTime, custID, tags);
		Customer customer = getCustomerByID(custID);
		customer.addSiteVisit(siteVisit);

		updateTimestamps(dateTime);
		return true;
	}

	private boolean ingestCustomer(JsonObject obj) {
		String custID = obj.getString(Constants.KEY, "");
		String eventTime = obj.getString(Constants.EVENT_TIME_KEY, "");
		String lastName = obj.getString(Constants.LAST_NAME_KEY, "");
		String city = obj.getString(Constants.ADR_CITY_KEY, "");
		String state = obj.getString(Constants.ADR_STATE_KEY, "");

		if ("".equals(custID)) {
			return false;
		}

		DateTime dateTime = null;
		if (!"".equals(eventTime)) {
			dateTime = ISODateTimeFormat.dateTime().withZoneUTC().parseDateTime(eventTime);
		}

		Customer customer = customers.get(custID);

		if (customer == null) {
			customer = new Customer(custID, dateTime, lastName, city, state);
			customers.put(custID, customer);
		} else {
			customer.updateCustomer(dateTime, lastName, city, state);
		}

		updateTimestamps(dateTime);

		return true;
	}

	public ArrayDeque<Customer> topXSimpleLTVCust(int x) {
		if(x<0 || dataCollectionEnd == null){
			//TODO
		}
		
		// Reuse the results if nothing has changed
		if(topx!=null && topx.size()==x && dataCollectionEnd!=null && lastCalculate==dataCollectionEnd){
			return topx;
		}
		
		
		lastCalculate = dataCollectionEnd;
		for(Customer cust: customers.values()) {
			cust.getAvgAmountPerWeek(dataCollectionEnd);
		}
		
		PriorityQueue<Customer> minHeap = new PriorityQueue<>(x, new Comparator<Customer>() {
			
			@Override
			public int compare(Customer left, Customer right) {
				double leftAvg = left.getAvgAmountPerWeek();
				double rightAvg = right.getAvgAmountPerWeek();
				if(leftAvg<rightAvg){
					return -1;
				} else if(leftAvg>rightAvg){
					return 1;
				} else{
					return 0;
				}
			}
		});
		
		
		for(Customer cust: customers.values()) {
			minHeap.offer(cust);
			if(minHeap.size()>x){
				minHeap.poll();
			}
		}
		
		topx = new ArrayDeque<>(x);
		while(!minHeap.isEmpty()) {
			topx.push(minHeap.poll());
		}
		
		return topx;
	}
}
