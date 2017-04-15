package com.qcloud.weapp.sql;

import java.sql.Date;

public class PostObject {
	
	public int id;
	public String title;
	public String content;
	public String type;
	public String city;
	public int userId;
	public Date createdDate;
	
	public PostObject(int id, String title, String content, String type, String city, int userId, Date createdDate) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.type = type;
		this.city = city;
		this.userId = userId;
		this.createdDate = createdDate;
	}

	public PostObject() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public String toString() {
		return "id: " + Integer.toString(id) + ", " 
				+ "title: " + title + ", "
				+ "content: " + content + ", "
				+ "city: " + city + ", "
				+ "type: " + type + ", "
				+ "userId: " + Integer.toString(userId) + ", "
				+ "createdDate: " + createdDate.toString();
	}
}
