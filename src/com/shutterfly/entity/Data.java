package com.shutterfly.entity;

import java.sql.Timestamp;
import java.util.Map;

import javax.json.JsonObject;

public class Data {

	private Map<String, Customer> customers;
	private Timestamp dataCollectionStart;
	private Timestamp dataCollectionEnd;

	public void ingest(JsonObject obj) {

	}

	public Map<String, Double> topXSimpleLTVCust() {

		return null;
	}
}
