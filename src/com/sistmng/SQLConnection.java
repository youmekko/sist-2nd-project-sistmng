package com.sistmng;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection {
	
	private static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String DB_URL = "jdbc:oracle:thin:@211.00.00.00:0000:xe";
	
	private static final String USER = "user"; //아이디
	private static final String PASS = "password"; //비밀번호
	
	private static Connection conn;
	
	public static final Connection connect() throws ClassNotFoundException, SQLException{
		
		Class.forName(JDBC_DRIVER);
		
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		
		return conn;
	}
	
	public static void close() throws SQLException{
		if(conn!=null) {
			conn.close();
		}
	}
}
