package com.shutterfly.entity;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import org.joda.time.DateTime;

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


	void updateTimestamps(DateTime dateTime) {
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

	Customer getCustomerByID(String custID, DateTime createTime) {
		Customer customer = customers.get(custID);
		if (customer == null) {
			customer = new Customer(custID, createTime);
			customers.put(custID, customer);
		}
		return customer;
	}

	Customer getCustomerByID(String custID) {
		return customers.get(custID);
	}
	
	void addCustomer(Customer customer) {
		if(customer!=null && customer.getKey()!=null){
			customers.put(customer.getKey(), customer);
		}
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
