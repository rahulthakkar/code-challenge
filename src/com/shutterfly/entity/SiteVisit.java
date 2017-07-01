package com.shutterfly.entity;

import java.util.Map;

import org.joda.time.DateTime;

public class SiteVisit extends EventEntity{
	
	private String custID;
	private Map<String, String> tags;
	
	
	public SiteVisit(String key, DateTime dateTime, String custID, Map<String, String> tags) {
		super(key, dateTime);
		this.custID = custID;
		this.tags = tags;
	}

	public String getCustID() {
		return custID;
	}

	public Map<String, String> getTags() {
		return tags;
	}

	@Override
	public String toString() {
		return "SiteVisit [pageID=" + getKey() + ", dateTime=" + getCreateTime() + ", custID=" + custID + ", tags=" + tags
				+ "]";
	}


	@Override
	public boolean ingest(Data data) {
		Customer customer = data.getCustomerByID(custID, getCreateTime());
		customer.addSiteVisit(this);
		data.updateTimestamps(getCreateTime());
		return true;
	}	
	
	
}
