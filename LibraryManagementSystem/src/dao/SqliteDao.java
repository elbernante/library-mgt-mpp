package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.base.DataAccessObject;

public class SqliteDao implements DataAccessObject {
	
	private static SqliteDao instance = null;
	private String url;
	private Connection connection;
	
	public static SqliteDao getInstance(String url) {
		if (instance == null) {
			instance = new SqliteDao(url);
		}
		return instance;
	}
	
	private SqliteDao(String dbfilename) {
		this.url = dbfilename;
	}
	
	@Override
	public void onLoad() {
		try {
			Class.forName("org.sqlite.JDBC");
			this.connection = DriverManager.getConnection("jdbc:sqlite:" + url);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(1);
		}
		
		System.out.println("Successfully connected to: " + url);
	}

	@Override
	public void onClose() {
		if (connection == null) return;
		try {
			connection.close();
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(1);
		}
		
		System.out.println("Successfully disconnected from: " + url);
	}

	@Override
	public void getPerson() {
		/* SQL query goes here */
		System.out.println("Getting person");
	}

	@Override
	public boolean authenticate(String username, String passwordHash) {
		String query = "SELECT username FROM user WHERE username=? AND password=? LIMIT 1";
		
		PreparedStatement stmt = null;
		boolean isAuthenticated = false;
		
		try {
			stmt = connection.prepareStatement(query);
			stmt.setString(1,  username);
			stmt.setString(2, passwordHash);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) isAuthenticated = true;			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} 
		
		return isAuthenticated;
	}
	
}
