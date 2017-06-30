package com.shutterfly.entity;

import java.sql.Timestamp;

public class Image {
	private String imageID;
	private Timestamp timestamp;
	private String custID;
	private String cameraMake;
	private String cameraModel;
	
	public Image(String imageID, Timestamp timestamp, String custID, String cameraMake, String cameraModel) {
		this.imageID = imageID;
		this.timestamp = timestamp;
		this.custID = custID;
		this.cameraMake = cameraMake;
		this.cameraModel = cameraModel;
	}

	public String getImageID() {
		return imageID;
	}

	public Timestamp getTimestamp() {
		return timestamp;
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
		return "Image [imageID=" + imageID + ", timestamp=" + timestamp + ", custID=" + custID + ", cameraMake="
				+ cameraMake + ", cameraModel=" + cameraModel + "]";
	}
	
	
	
}
