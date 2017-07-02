package com.shutterfly.entity;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.shutterfly.io.EventInputReader;

public class OrderTest {
	private Data data;
	
	@Before
	public void setUp() throws Exception {
		String inputPath = "src\\test\\resources\\input.txt";
		EventInputReader evReader = new EventInputReader();
		data = evReader.populateEvents(inputPath);
	}

	@Test
	public void ingestNewOrder() {
		String custID = "96f55c7d8f42"; 
		Customer customer = data.getCustomerByID(custID);
		double oldCustomerTotalOrderAmount = customer.getTotalOrderAmount(); 
		
		String orderID = "a6f55c7d8f42";
		double orderAmount = 100.0;
		DateTime orderTime = new DateTime("2020-01-06T12:46:46.384Z");
		
		Order newOrder = new Order(orderID, orderTime, custID, orderAmount);
		newOrder.ingest(data);
		assertEquals(newOrder, customer.getOrders().get(orderID));
		assertEquals(oldCustomerTotalOrderAmount+orderAmount, customer.getTotalOrderAmount(), 0.0001);
		assertEquals(oldCustomerTotalOrderAmount+orderAmount, customer.getTotalOrderAmount(), 0.0001);
		assertEquals(orderTime, customer.getLastUpdateTime());
		assertEquals(orderTime, data.getDataCollectionEnd());
	}
	
	@Test
	public void ingestOrderUpdate() {
		Customer customer = data.getCustomerByID("96f55c7d8f42");
		Order oldOrder = customer.getOrders().get("68d84e5d1a43");
		
		double newAmount = 100.0;
		double oldAmount = oldOrder.getAmount();
		double oldCustomerTotalOrderAmount = customer.getTotalOrderAmount(); 
		
		Order updatedOrder = new Order(oldOrder.getKey(), oldOrder.getLastUpdateTime().plusMinutes(1), oldOrder.getCustID(), newAmount);
		updatedOrder.ingest(data);
		assertEquals(updatedOrder.getLastUpdateTime(), oldOrder.getLastUpdateTime());
		assertEquals(newAmount, oldOrder.getAmount(), 0.0001);
		assertEquals(oldCustomerTotalOrderAmount - oldAmount + newAmount, customer.getTotalOrderAmount(), 0.0001);
	}

}
