package com.shutterfly.entity;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joda.time.DateTime;

public class Data {

	private static final Logger LOGGER = Logger.getLogger(Data.class.getName());

	private Map<String, Customer> customers;
	private DateTime dataCollectionStart;
	private DateTime dataCollectionEnd;
	private ArrayDeque<Customer> topx;

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

	/*
	 * Creates a new customer if not already present in Data - Used when
	 * customer update or event belonging to customer arrives before customer
	 * creation
	 */
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
		if (customer != null && customer.getKey() != null) {
			customers.put(customer.getKey(), customer);
		}
	}

	/*
	 * Returns X customer highest avg weekly spending - Puts data on min heap of
	 * size x
	 */
	public ArrayDeque<Customer> topXSimpleLTVCust(int x) {
		if (x < 0) {
			LOGGER.log(Level.SEVERE, "x is less than zero x={0}", x);
			return new ArrayDeque<>();
		}
		if (dataCollectionEnd == null) {
			LOGGER.log(Level.SEVERE, "Can't calculate as the end time is null");
			return new ArrayDeque<>();
		}

		DateTime lastCalculate = dataCollectionEnd;

		PriorityQueue<Customer> minHeap = new PriorityQueue<>(x, new Comparator<Customer>() {

			@Override
			public int compare(Customer left, Customer right) {
				double leftAvg = left.getAvgAmountPerWeek();
				double rightAvg = right.getAvgAmountPerWeek();
				if (leftAvg < rightAvg) {
					return -1;
				} else if (leftAvg > rightAvg) {
					return 1;
				} else {
					return 0;
				}
			}
		});

		// Calculate avg spending for all customer for the given endDateTime
		// Avoids repeat calculation of calling every time from comparator
		for (Customer cust : customers.values()) {
			cust.getAvgAmountPerWeek(lastCalculate);
		}

		for (Customer cust : customers.values()) {
			minHeap.offer(cust);
			// Smallest value out of top x is on root 
			if (minHeap.size() > x) {
				minHeap.poll();
			}
		}

		topx = new ArrayDeque<>(x);
		while (!minHeap.isEmpty()) {
			topx.push(minHeap.poll());
		}

		return topx;
	}
}
