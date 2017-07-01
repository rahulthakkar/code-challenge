package com.shutterfly.entity;

import java.util.Map;

import org.joda.time.DateTime;

public class SiteVisit {
	private String pageID;
	private DateTime dateTime;
	private String custID;
	private Map<String, String> tags;
	
	
	public SiteVisit(String pageID, DateTime dateTime, String custID, Map<String, String> tags) {
		this.pageID = pageID;
		this.dateTime = dateTime;
		this.custID = custID;
		this.tags = tags;
	}


	public String getPageID() {
		return pageID;
	}

	public DateTime getDateTime() {
		return dateTime;
	}

	public String getCustID() {
		return custID;
	}

	public Map<String, String> getTags() {
		return tags;
	}


	@Override
	public String toString() {
		return "SiteVisit [pageID=" + pageID + ", dateTime=" + dateTime + ", custID=" + custID + ", tags=" + tags
				+ "]";
	}	
	
	
}
