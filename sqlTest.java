package com.qcloud.weapp;

import java.sql.Date;
import java.sql.SQLException;

import com.qcloud.weapp.dao.PostDAO;
import com.qcloud.weapp.dao.UserDAO;
import com.qcloud.weapp.sql.PostObject;
import com.qcloud.weapp.sql.UserObject;

public class sqlTest {
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		UserDAO userDao = new UserDAO();
		PostDAO postDao = new PostDAO();
		//postTest(userDao, postDao);
		getTest(userDao, postDao);
		updateTest();
		deleteTest();
	}

	private static void getTest(UserDAO userDao, PostDAO postDao) throws ClassNotFoundException
														, SQLException {
		UserObject obj = userDao.getUserById(1);
		if (obj != null) System.out.println(obj.toString());
		PostObject postObj = postDao.getPostById(2);
		if (postObj != null) System.out.println(postObj.toString());
	}
	
	private static void postTest(UserDAO userDao, PostDAO postDao) throws SQLException {
		UserObject userObj = new UserObject();
		userObj.setOpenId("abcdef8");
		userObj.setCreatedDate(new Date(System.currentTimeMillis()));
		userDao.saveUserObject(userObj);
		
		PostObject postObj = new PostObject();
		postObj.setTitle("A dummy title");
		postObj.setCity("SEATTLE");
		postObj.setType("1B1B");
		postObj.setUserId(1);
		postObj.setContent("A dummy content");
		postObj.setCreatedDate(new Date(System.currentTimeMillis()));
		postDao.savePostObject(postObj);
	}
	
	private static void deleteTest() {
		// TODO Auto-generated method stub
		
	}

	private static void updateTest() {
		// TODO Auto-generated method stub
		
	}
}
