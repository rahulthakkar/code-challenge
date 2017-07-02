package com.shutterfly.entity;

import org.joda.time.DateTime;

public abstract class UpdatableEventEntity extends EventEntity {

	private DateTime lastUpdateTime;

	public UpdatableEventEntity(String key, DateTime createTime) {
		super(key, createTime);
		this.lastUpdateTime = createTime;
	}

	public DateTime getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(DateTime dateTime) {
		if (dateTime != null) {
			if (this.lastUpdateTime == null || dateTime.isAfter(this.lastUpdateTime)) {
				this.lastUpdateTime = dateTime;
			}
		}
	}

}
