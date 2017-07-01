package com.shutterfly.entity;

import org.joda.time.DateTime;

public class Image extends EventEntity {
	
	private String custID;
	private String cameraMake;
	private String cameraModel;
	
	public Image(String key, DateTime dateTime, String custID, String cameraMake, String cameraModel) {
		super(key, dateTime);
		this.custID = custID;
		this.cameraMake = cameraMake;
		this.cameraModel = cameraModel;
	}

	public String getCustID() {
		return custID;
	}

	public String getCameraMake() {
		return cameraMake;
	}

	public String getCameraModel() {
		return cameraModel;
	}

	@Override
	public String toString() {
		return "Image [imageID=" + getKey() + ", dateTime=" + getCreateTime() + ", custID=" + custID + ", cameraMake="
				+ cameraMake + ", cameraModel=" + cameraModel + "]";
	}

	@Override
	public boolean ingest(Data data) {
		Customer customer = data.getCustomerByID(custID, getCreateTime());
		customer.addImage(this);
		data.updateTimestamps(getCreateTime());
		return true;
	}
	
	
	
}
