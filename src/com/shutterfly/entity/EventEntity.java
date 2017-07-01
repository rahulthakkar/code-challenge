package com.shutterfly.entity;

import org.joda.time.DateTime;

public abstract class EventEntity {
	private String key;
	private DateTime createTime;
	
	public EventEntity(String key, DateTime createTime) {
		this.key = key;
		this.createTime = createTime;
	}
	
	abstract public boolean ingest(Data data);

	public String getKey() {
		return key;
	}

	public DateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(DateTime dateTime) {
		if(dateTime!=null){
			if(this.createTime==null || dateTime.isBefore(this.createTime)){
					this.createTime = dateTime;	
			}
		}
	} 
	
}
