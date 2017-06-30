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
		
}
