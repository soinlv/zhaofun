package com.qcloud.weapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DAO {
	
	public String url;
	public String user;
	public String pwd;
	public static Connection conn;
	
	private static final Logger logger = Logger.getLogger(DAO.class.getName());
	
	public DAO() throws SQLException, ClassNotFoundException {
		//TODO: get the values from property file.
		//this.url = "jdbc:mysql://58f1b68865215.sh.cdb.myqcloud.com:7280/zhaofun";
		this.url = "jdbc:mysql://10.66.100.28:3306/test";
		//this.user = "cdb_outerroot";
		this.user = "session_user";
		//this.pwd = "Uc!513sql";
		this.pwd = "Es5BJQX6g4";
		String driverClass = "com.mysql.jdbc.Driver";
		try {
			Class.forName(driverClass);
		}
		catch (ClassNotFoundException e1) {
			logger.log(Level.SEVERE, "Failed to find the class" + driverClass
					, e1);
			throw e1;
		}
		try {
			conn = DriverManager.getConnection(url, user, pwd);
		}
		catch (SQLException e2) {
			logger.log(Level.SEVERE, "Failed to connect to data source", e2);
			throw e2;
		}
	}
}