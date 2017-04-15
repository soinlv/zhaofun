package com.qcloud.weapp.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.qcloud.weapp.sql.UserObject;

public class UserDAO extends DAO{
		
	public UserDAO() throws SQLException, ClassNotFoundException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public UserObject getUserById(int id) throws SQLException {
		UserObject userObj = new UserObject();
		PreparedStatement stmt = conn.prepareStatement("select * from user where id = ?");
		stmt.setInt(1, id);
		ResultSet result = stmt.executeQuery();
	    if (!result.next()) return null;
	    userObj.setId(id);
	    userObj.setOpenId(result.getString("openId"));
	    userObj.setCreatedDate(result.getDate("createdDate"));
	    stmt.close();
	    conn.close();
		return userObj;
	}
	
	public void saveUserObject(UserObject obj) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("INSERT INTO user (openId, createdDate)"
														+ " VALUES (?, ?)");
		stmt.setString(1, obj.getOpenId());
		stmt.setDate(2, obj.getCreatedDate());
		stmt.executeUpdate();
		stmt.close();
		conn.close();
	}
}
