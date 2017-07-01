package com.shutterfly.io;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import com.shutterfly.entity.Customer;
import com.shutterfly.entity.EventEntity;
import com.shutterfly.entity.Image;
import com.shutterfly.entity.Order;
import com.shutterfly.entity.SiteVisit;
import com.shutterfly.utility.Constants;

public class EventEntityFactory {

	public static EventEntity getEvent(JsonObject obj) {
		String eventType = obj.getString(Constants.TYPE_KEY, "");

		switch (eventType) {
		case Constants.EVENT_TYPE_CUSTOMER:
			return getCustomer(obj);

		case Constants.EVENT_TYPE_SITE_VISIT:
			return getSiteVisit(obj);

		case Constants.EVENT_TYPE_IMAGE:
			return getImage(obj);

		case Constants.EVENT_TYPE_ORDER:
			return getOrder(obj);
		default:
			// TODO - LOG
			return null;
		}
	}

	private static EventEntity getOrder(JsonObject obj) {
		String orderID = obj.getString(Constants.KEY, "");
		String eventTime = obj.getString(Constants.EVENT_TIME_KEY, "");
		String custID = obj.getString(Constants.CUSTOMER_ID_KEY, "defaultID");

		// Dropping anything but digits and . with assumption
		// - there will be only one .
		// - order amount is not negative
		// - all the orders are in same currency
		double amount = Double.parseDouble(obj.getString(Constants.TOTAL_AMOUNT_KEY, "0.0").replaceAll("[^\\d.]", ""));

		if ("".equals(orderID)) {
			// TODO log
			return null;
		}

		DateTime dateTime = null;
		if (!"".equals(eventTime)) {
			dateTime = ISODateTimeFormat.dateTime().withZoneUTC().parseDateTime(eventTime);
		}

		return new Order(orderID, dateTime, custID, amount);
	}

	private static EventEntity getImage(JsonObject obj) {
		String imageID = obj.getString(Constants.KEY, "");
		String eventTime = obj.getString(Constants.EVENT_TIME_KEY, "");
		String custID = obj.getString(Constants.CUSTOMER_ID_KEY, "defaultID");
		String cameraMake = obj.getString(Constants.CAMERA_MAKE_KEY, "");
		String cameraModel = obj.getString(Constants.CAMERA_MODEL_KEY, "");

		if ("".equals(imageID)) {
			// TODO log
			return null;
		}

		DateTime dateTime = null;
		if (!"".equals(eventTime)) {
			dateTime = ISODateTimeFormat.dateTime().withZoneUTC().parseDateTime(eventTime);
		}

		return new Image(imageID, dateTime, custID, cameraMake, cameraModel);
	}

	private static EventEntity getSiteVisit(JsonObject obj) {
		String pageID = obj.getString(Constants.KEY, "");
		String eventTime = obj.getString(Constants.EVENT_TIME_KEY, "");
		String custID = obj.getString(Constants.CUSTOMER_ID_KEY, "defaultID");
		JsonArray tagsArray = obj.getJsonArray(Constants.TAGS_KEY);

		if ("".equals(pageID)) {
			// TODO log
			return null;
		}

		Map<String, String> tags = new HashMap<>();

		for (JsonValue value : tagsArray) {
			JsonObject object = (JsonObject) value;
			Set<String> keySet = object.keySet();
			for (String key : keySet) {
				tags.put(key, object.getString(key, ""));
			}
		}

		DateTime dateTime = null;
		if (!"".equals(eventTime)) {
			dateTime = ISODateTimeFormat.dateTime().withZoneUTC().parseDateTime(eventTime);
		}

		return new SiteVisit(pageID, dateTime, custID, tags);
	}

	private static EventEntity getCustomer(JsonObject obj) {
		String custID = obj.getString(Constants.KEY, "");
		String eventTime = obj.getString(Constants.EVENT_TIME_KEY, "");
		String lastName = obj.getString(Constants.LAST_NAME_KEY, "");
		String city = obj.getString(Constants.ADR_CITY_KEY, "");
		String state = obj.getString(Constants.ADR_STATE_KEY, "");

		if ("".equals(custID)) {
			// TODO log
			return null;
		}

		DateTime dateTime = null;
		if (!"".equals(eventTime)) {
			dateTime = ISODateTimeFormat.dateTime().withZoneUTC().parseDateTime(eventTime);
		}

		return new Customer(custID, dateTime, lastName, city, state);
	}
}
