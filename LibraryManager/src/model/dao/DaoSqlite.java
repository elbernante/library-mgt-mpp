package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import model.dao.base.DataAccessObject;

public class DaoSqlite implements DataAccessObject {
	
	private static DaoSqlite instance = null;
	private String url;
	private Connection connection;
	
	public static DaoSqlite getInstance(String url) {
		if (instance == null) {
			instance = new DaoSqlite(url);
		}
		return instance;
	}
	
	private DaoSqlite(String dbfilename) {
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
	
}
