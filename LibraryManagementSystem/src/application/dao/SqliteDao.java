package application.dao;

import application.dao.base.DataAccessObject;
import application.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

	@Override
	public List<Book> findAllBooks() {
		List<Book> results = new ArrayList<>();
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM book");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String isbn = rs.getString("isbn");
				String title = rs.getString("title");
				List<Author> authors = findAuthorsByIsbn(isbn);
				List<BookCopy> bookCopies = findCopiesByIsbn(isbn);

				Book book = new Book(isbn, title);
				book.setAuthors(authors);

				for (BookCopy bookCopy : bookCopies) {
					book.addCopy(bookCopy);
				}

				results.add(book);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}

	@Override
	public List<Author> findAuthorsByIsbn(String isbn) {
		List<Author> results = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = connection.prepareStatement("SELECT *\n" +
					"FROM book_author ba\n" +
					"LEFT JOIN author a ON ba.author_id = a.author_id\n" +
					"WHERE book_isbn = ?");
			stmt.setString(1, isbn);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String firstName = rs.getString("firstname");
				String lastName = rs.getString("lastname");
				String street = rs.getString("street");
				String city = rs.getString("city");
				String state = rs.getString("state");
				String zip = rs.getString("zip");
				String phoneNumber = rs.getString("phone");
				String credentials = rs.getString("credentials");
				String bio = rs.getString("bio");

				Address address = new Address(street, city, state, zip);
				Author author = new Author(firstName, lastName, address, phoneNumber, credentials, bio);

				results.add(author);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return results;
	}

	@Override
	public List<BookCopy> findCopiesByIsbn(String isbn) {
		List<BookCopy> results = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = connection.prepareStatement("SELECT *\n" +
					"FROM book_copy bc\n" +
					"WHERE bc.book_isbn = ?");
			stmt.setString(1, isbn);
			rs = stmt.executeQuery();
			while (rs.next()) {
				BookCopy bookCopy = new BookCopy(rs.getString("copy_id"), rs.getInt("available") == 1);
				results.add(bookCopy);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return results;
	}

}
