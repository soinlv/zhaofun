package com.qcloud.weapp.sql;

import java.sql.Date;

public class UserObject {
	
	public int id;
	public String openId;
	public Date createdDate;
	
	
	
	public UserObject(int id, String openId, Date createdDate) {
		
		this.id = id;
		this.openId = openId;
		this.createdDate = createdDate;
	}
	
	public UserObject() {
		
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String toString() {
		return "id: " + Integer.toString(id) + ", " 
				+ "openId: " + openId + ", "
				+ "createdDate: " + createdDate.toString();
	}
	
}
