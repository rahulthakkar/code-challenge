package com.shutterfly.entity;

import org.joda.time.DateTime;

public class Image {
	private String imageID;
	private DateTime dateTime;
	private String custID;
	private String cameraMake;
	private String cameraModel;
	
	public Image(String imageID, DateTime dateTime, String custID, String cameraMake, String cameraModel) {
		this.imageID = imageID;
		this.dateTime = dateTime;
		this.custID = custID;
		this.cameraMake = cameraMake;
		this.cameraModel = cameraModel;
	}

	public String getImageID() {
		return imageID;
	}

	public DateTime getDateTime() {
		return dateTime;
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
		return "Image [imageID=" + imageID + ", dateTime=" + dateTime + ", custID=" + custID + ", cameraMake="
				+ cameraMake + ", cameraModel=" + cameraModel + "]";
	}
	
	
	
}
