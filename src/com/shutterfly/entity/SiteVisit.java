package com.shutterfly.entity;

import java.sql.Timestamp;
import java.util.Map;

public class SiteVisit {
	private String pageID;
	private Timestamp timestamp;
	private String custID;
	private Map<String, String> tags;
	
	
	public SiteVisit(String pageID, Timestamp timestamp, String custID, Map<String, String> tags) {
		this.pageID = pageID;
		this.timestamp = timestamp;
		this.custID = custID;
		this.tags = tags;
	}


	public String getPageID() {
		return pageID;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public String getCustID() {
		return custID;
	}

	public Map<String, String> getTags() {
		return tags;
	}


	@Override
	public String toString() {
		return "SiteVisit [pageID=" + pageID + ", timestamp=" + timestamp + ", custID=" + custID + ", tags=" + tags
				+ "]";
	}	
	
	
}
