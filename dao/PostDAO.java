package com.qcloud.weapp.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.qcloud.weapp.sql.PostObject;

public class PostDAO extends DAO{

	public PostDAO() throws SQLException, ClassNotFoundException {
		
	}
	
	public PostObject getPostById(int id) throws SQLException {
		PostObject postObj = new PostObject();
		PreparedStatement stmt = conn.prepareStatement("select * from post where id = ?");
		stmt.setInt(1, id);
		ResultSet result = stmt.executeQuery();
	    if (!result.next()) return null;
	    postObj.setId(id);
	    postObj.setTitle(result.getString("title"));
	    postObj.setContent(result.getString("content"));
	    postObj.setType(result.getString("type"));
	    postObj.setCity(result.getString("city"));
	    postObj.setUserId(result.getInt("userId"));
	    postObj.setCreatedDate(result.getDate("createdDate"));
	    stmt.close();
	    conn.close();
		return postObj;
	}
	
	public void savePostObject(PostObject postObj) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("INSERT INTO post (title, content"
				+ ", type, city, userId, createdDate)"
				+ " VALUES (?, ?, ?, ?, ?, ?)");
		stmt.setString(1, postObj.getTitle());
		stmt.setString(2, postObj.getContent());
		stmt.setString(3, postObj.getType());
		stmt.setString(4, postObj.getCity());
		stmt.setInt(5, postObj.getUserId());
		stmt.setDate(6, postObj.getCreatedDate());
		stmt.executeUpdate();
		stmt.close();
		conn.close();
	}
}