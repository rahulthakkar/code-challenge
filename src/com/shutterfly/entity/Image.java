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
	
	
}
