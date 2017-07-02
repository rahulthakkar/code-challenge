/**
 * 
 */
package com.shutterfly.entity;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.shutterfly.io.EventInputReader;

public class CustomerTest {

	private Data data;
	
	@Before
	public void setUp() throws Exception {
		String inputPath = "src\\test\\resources\\input.txt";
		EventInputReader evReader = new EventInputReader();
		data = evReader.populateEvents(inputPath);
	}

	@Test
	public void ingestNewCustomer() {
		String custID = "az6f55c7d8f4";
		DateTime creationTime = new DateTime("2010-01-06T12:46:46.384Z");
		
		String orderID = "or6f55c7d8f4";
		

		Customer newCustomer = new Customer(custID, creationTime, "Walsh", "Boston", "MA");
		newCustomer.ingest(data);
		assertEquals(newCustomer, data.getCustomerByID(custID));
		
		double newAmount = 100.0;
		DateTime orderTime = creationTime.plusMinutes(1);
		Order newOrder = new Order(orderID, orderTime, custID, newAmount);
		newOrder.ingest(data);
		assertEquals(orderTime, data.getCustomerByID(custID).getLastUpdateTime());
		assertEquals(newAmount, data.getCustomerByID(custID).getTotalOrderAmount(), 0.0001);
	}

	@Test
	public void ingestCustomerLatestUpdate() {
		String custID = "az6f55c7d8f5";
		DateTime creationTime = new DateTime("2010-01-06T12:46:46.384Z");
		
		Customer newCustomer = new Customer(custID, creationTime, "Walsh", "Boston", "MA");
		newCustomer.ingest(data);
		
		DateTime updateTime = creationTime.plusYears(1);
		String newCity = "New York";
		String newState = "NY";
		
		Customer updatedCustomer = new Customer(custID, updateTime, "Walsh", newCity, newState);
		updatedCustomer.ingest(data);
		
		assertEquals(updateTime, data.getCustomerByID(custID).getLastUpdateTime());
		assertEquals(newCity, data.getCustomerByID(custID).getCity());
		assertEquals(newState, data.getCustomerByID(custID).getState());
	}
	
	@Test
	public void ingestCustomerOldUpdate() {
		String custID = "bz6f55c7d8f5";
		DateTime creationTime = new DateTime("2010-01-06T12:46:46.384Z");
		
		Customer newCustomer = new Customer(custID, creationTime, "Walsh", "Boston", "MA");
		newCustomer.ingest(data);
		
		DateTime updateTime = creationTime.plusYears(1);
		String newCity = "New York";
		String newState = "NY";
		
		Customer updatedCustomer = new Customer(custID, updateTime, "Walsh", newCity, newState);
		updatedCustomer.ingest(data);
		
		DateTime missedUpdateTime = creationTime.plusMonths(6);
		String missedCity = "Jersey City";
		String missedState = "NJ";
		
		Customer missedUpdatedCustomer = new Customer(custID, missedUpdateTime, "Walsh", missedCity, missedState);
		missedUpdatedCustomer.ingest(data);
		
		assertEquals(updateTime, data.getCustomerByID(custID).getLastUpdateTime());
		assertEquals(newCity, data.getCustomerByID(custID).getCity());
		assertEquals(newState, data.getCustomerByID(custID).getState());
	}
	
	

}
