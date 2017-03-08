package application.dao;

import application.dao.base.DataAccessObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.*;

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
	public boolean authenticate(String userId, String passwordHash) {
		String query = "SELECT user_id FROM user WHERE user_id=? AND password=? LIMIT 1";
		
		PreparedStatement stmt = null;
		boolean isAuthenticated = false;
		
		try {
			stmt = connection.prepareStatement(query);
			stmt.setString(1, userId);
			stmt.setString(2, passwordHash);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) isAuthenticated = true;			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} 
		
		return isAuthenticated;
	}

	@Override
	public User getUserById(String userId) {
		String user_query = "SELECT user_id, password, firstname, lastname  FROM user WHERE user_id=? LIMIT 1";
		String role_query = "SELECT role_id FROM user_role WHERE user_id=?";

		User user = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = connection.prepareStatement(user_query);
			stmt.setString(1, userId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				user = new User();
				user.setUserId(rs.getString("user_id"));
				user.setPassword(rs.getString("password"));
				user.setFirstName(rs.getString("firstname"));
				user.setLastName(rs.getString("lastname"));
				stmt.close();
				rs.close();
				
				stmt = connection.prepareStatement(role_query);
				stmt.setString(1, userId);
				rs = stmt.executeQuery();
				while (rs.next()) user.addRole(rs.getInt("role_id"));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return user;
	}
}
