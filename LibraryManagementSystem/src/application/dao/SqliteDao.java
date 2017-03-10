package application.dao;

import application.dao.base.DataAccessObject;
import application.model.*;

import java.sql.*;
import java.time.LocalDate;
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
		User user = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String userQuery = "SELECT user_id, password, firstname, lastname  FROM user WHERE user_id=? LIMIT 1";
			stmt = connection.prepareStatement(userQuery);
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

				// get roles
				String roleQuery = "SELECT role_id FROM user_role WHERE user_id=?";
				stmt = connection.prepareStatement(roleQuery);
				stmt.setString(1, userId);
				rs = stmt.executeQuery();
				while (rs.next()) user.addRole(rs.getInt("role_id"));
				stmt.close();
				rs.close();

				// get address
				String addressQuery = "SELECT street, city, state, zip, phone FROM address WHERE user_id=? LIMIT 1";
				user.setAddress(new UserAddress());
				stmt = connection.prepareStatement(addressQuery);
				stmt.setString(1, userId);
				rs = stmt.executeQuery();
				if (rs.next()) {
					UserAddress address = user.getAddress();
					address.setUserId(userId);
					address.setStreet(rs.getString("street"));
					address.setCity(rs.getString("city"));
					address.setState(rs.getString("state"));
					address.setZip(rs.getString("zip"));
					address.setPhone(rs.getString("phone"));
				}
				stmt.close();
				rs.close();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return user;
	}

	@Override
	public List<User> findAllUsers() {
		List<User> users = new ArrayList<>();

		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String userQuery = "SELECT user_id FROM user";
			stmt = connection.prepareStatement(userQuery);
			rs = stmt.executeQuery();

			while (rs.next()) users.add(getUserById(rs.getString("user_id")));

			stmt.close();
			rs.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return users;
	}

	@Override
	public boolean saveNewUser(User user) {
		boolean retVal = false;

		PreparedStatement stmt = null;

		try {
			// save user
			String saveUser = "INSERT INTO 'user'(user_id, password, firstname, lastname) VALUES (?, ?, ?, ?)";
			stmt = connection.prepareStatement(saveUser);
			stmt.setString(1, user.getUserId());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getFirstName());
			stmt.setString(4, user.getLastName());
			stmt.executeUpdate();
			stmt.close();

			// save role
			String saveRole = "INSERT INTO user_role(user_id, role_id) VALUES (?, ?)";
			for (Integer i : user.getRoles()) {
				stmt = connection.prepareStatement(saveRole);
				stmt.setString(1, user.getUserId());
				stmt.setInt(2, i);
				stmt.executeUpdate();
				stmt.close();
			}

			// save address
			String saveAddress = "INSERT INTO address(user_id, street, city, state, zip, phone) VALUES (?, ?, ?, ?, ?, ?)";
			stmt = connection.prepareStatement(saveAddress);
			stmt.setString(1, user.getUserId());
			stmt.setString(2, user.getAddress().getStreet());
			stmt.setString(3, user.getAddress().getCity());
			stmt.setString(4, user.getAddress().getState());
			stmt.setString(5, user.getAddress().getZip());
			stmt.setString(6, user.getAddress().getPhone());
			stmt.executeUpdate();
			stmt.close();

			retVal = true;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		return retVal;
	}

	@Override
	public List<Book> findAllBooks() throws SQLException {
		List<Book> results = new ArrayList<>();
		PreparedStatement stmt = connection.prepareStatement("SELECT * FROM book");
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			String isbn = rs.getString("isbn");
			String title = rs.getString("title");
			int checkoutLimit = rs.getInt("checkout_limit");
			List<Author> authors = findAuthorsByIsbn(isbn);
			List<BookCopy> bookCopies = findCopiesByIsbn(isbn);

			Book book = new Book(isbn, title, checkoutLimit);
			book.setAuthors(authors);

			for (BookCopy bookCopy : bookCopies) {
				book.addCopy(bookCopy);
			}

			results.add(book);
		}
		stmt.close();
		rs.close();
		return results;
	}

	@Override
	public Book findBookByIsbn(String isbn) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		stmt = connection.prepareStatement("SELECT * FROM book WHERE isbn = ?");
		stmt.setString(1, isbn);
		rs = stmt.executeQuery();
		if (rs.next()) {
			return new Book(rs.getString("isbn"), rs.getString("title"), rs.getInt("checkout_limit"));
		}
		stmt.close();
		rs.close();

		return null;
	}
	
	@Override
	public Book getBookByIsbn(String isbn) {
		Book book = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String bookQuery = "SELECT title, checkout_limit FROM book WHERE isbn=? LIMIT 1";
			stmt = connection.prepareStatement(bookQuery);
			stmt.setString(1, isbn);
			rs = stmt.executeQuery();

			if (rs.next()) {
				book = new Book(isbn, rs.getString("title"), rs.getInt("checkout_limit"));
				List<Author> authors = findAuthorsByIsbn(isbn);
				List<BookCopy> bookCopies = findCopiesByIsbn(isbn);
				
				book.setAuthors(authors);

				for (BookCopy bookCopy : bookCopies) {
					book.addCopy(bookCopy);
				}
			}
			stmt.close();
			rs.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return book;
	}
	
	@Override
	public BookCopy getBookCopyById(int id) {
		BookCopy copy = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String query = "SELECT copy_id, book_isbn, available FROM book_copy WHERE copy_id=? LIMIT 1";
			stmt = connection.prepareStatement(query);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();

			if (rs.next()) {
				copy = new BookCopy(rs.getInt("copy_id"), rs.getInt("available") == 1);
				copy.setIsbn(rs.getString("book_isbn"));
			}
			stmt.close();
			rs.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return copy;
	}

	@Override
	public List<Author> findAuthorsByIsbn(String isbn) throws SQLException {
		String sql = "SELECT *\n" +
				"FROM book_author ba\n" +
				"LEFT JOIN author a ON ba.author_id = a.author_id\n" +
				"WHERE book_isbn = ?";
		List<Author> results = new ArrayList<>();
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, isbn);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			Integer id = rs.getInt("author_id");
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
			author.setId(id);

			results.add(author);
		}
		stmt.close();
		rs.close();
		return results;
	}

	@Override
	public List<BookCopy> findCopiesByIsbn(String isbn) throws SQLException {
		String sql = "SELECT * FROM book_copy bc WHERE bc.book_isbn = ?";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, isbn);
		ResultSet rs = stmt.executeQuery();
		List<BookCopy> results = new ArrayList<>();
		while (rs.next()) {
			BookCopy bookCopy = new BookCopy(rs.getInt("copy_id"), rs.getInt("available") == 1);
			results.add(bookCopy);
		}
		stmt.close();
		rs.close();
		return results;
	}

	@Override
	public void createBook(Book book) throws SQLException {
		String sql = "INSERT INTO book (isbn, title, checkout_limit) VALUES (?, ?, ?)";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, book.getIsbn());
		stmt.setString(2, book.getTitle());
		stmt.setInt(3, book.getCheckoutLimit());
		stmt.execute();

		if (book.getAuthors() != null) {
			for (Author author : book.getAuthors()) {
				stmt = connection.prepareStatement("INSERT INTO book_author (book_isbn, author_id) VALUES (?, ?)");
				stmt.setString(1, book.getIsbn());
				stmt.setInt(2, author.getId());
				stmt.execute();
			}
		}

		stmt.close();
	}

	@Override
	public void updateBook(Book book) throws SQLException {
		String sql = "UPDATE book SET title = ? WHERE isbn = ?";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, book.getTitle());
		stmt.setString(2, book.getIsbn());
		stmt.execute();

		stmt = connection.prepareStatement("DELETE FROM book_author WHERE book_isbn = ?");
		stmt.setString(1, book.getIsbn());
		stmt.execute();

		if (book.getAuthors() != null) {
			for (Author author : book.getAuthors()) {
				stmt = connection.prepareStatement("INSERT INTO book_author (book_isbn, author_id) VALUES (?, ?)");
				stmt.setString(1, book.getIsbn());
				stmt.setInt(2, author.getId());
				stmt.execute();
			}
		}

		stmt.close();
	}

	@Override
	public List<Author> findAllAuthors() throws SQLException {
		List<Author> results = new ArrayList<>();
		PreparedStatement stmt = connection.prepareStatement("SELECT * FROM author");
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			Author author = new Author(
					rs.getString("firstname"),
					rs.getString("lastname"),
					new Address(rs.getString("street"), rs.getString("city"), rs.getString("state"), rs.getString("zip")),
					rs.getString("phone"),
					rs.getString("credentials"),
					rs.getString("bio")
			);
			author.setId(rs.getInt("author_id"));
			results.add(author);
		}
		stmt.close();
		rs.close();
		return results;
	}

	@Override
	public void createAuthor(Author author) throws SQLException {
		String sql = "INSERT INTO author (firstname, lastname, street, city, state, zip, phone, credentials, bio) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, author.getFirstName());
		stmt.setString(2, author.getLastName());
		stmt.setString(3, author.getAddress().getStreet());
		stmt.setString(4, author.getAddress().getCity());
		stmt.setString(5, author.getAddress().getState());
		stmt.setString(6, author.getAddress().getZip());
		stmt.setString(7, author.getPhoneNumber());
		stmt.setString(8, author.getCredentials());
		stmt.setString(9, author.getShortBio());
		stmt.execute();
		stmt.close();
	}

	@Override
	public void updateAuthor(Author author) throws SQLException {
		String sql = "UPDATE author SET " +
				"firstname = ?, " +
				"lastname = ?, " +
				"street = ?, " +
				"city = ?, " +
				"state = ?, " +
				"zip = ?, " +
				"phone = ?, " +
				"credentials = ?, " +
				"bio = ?" +
				"WHERE author_id = ?";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, author.getFirstName());
		stmt.setString(2, author.getLastName());
		stmt.setString(3, author.getAddress().getStreet());
		stmt.setString(4, author.getAddress().getCity());
		stmt.setString(5, author.getAddress().getState());
		stmt.setString(6, author.getAddress().getZip());
		stmt.setString(7, author.getPhoneNumber());
		stmt.setString(8, author.getCredentials());
		stmt.setString(9, author.getShortBio());
		stmt.setInt(10, author.getId());
		stmt.execute();
		stmt.close();
	}

	@Override
	public void createBookCopy(BookCopy bookCopy) throws SQLException {
		String sql = "INSERT INTO book_copy (book_isbn, available) VALUES (?, ?)";
		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, bookCopy.getBook().getIsbn());
		stmt.setInt(2, 1);
		stmt.execute();
		stmt.close();
	}
	
	@Override
	public CheckoutEntry checkoutCopy(String userId, int copyId) {
		CheckoutEntry entry = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		int checkoutLimit = 0;
		try {
			// get limit
			String limitQuery = "SELECT checkout_limit FROM book WHERE isbn = " + 
								"(SELECT book_isbn FROM book_copy WHERE copy_id=? LIMIT 1) LIMIT 1";
			stmt = connection.prepareStatement(limitQuery);
			stmt.setInt(1, copyId);
			rs = stmt.executeQuery();
			if (rs.next()) checkoutLimit = rs.getInt("checkout_limit");
			stmt.close();
			rs.close();
			
			// create entry
			String createEntry = "INSERT INTO checkout_entry (user_id, copy_id, checkout_date, due_date) VALUES (?, ?, ?, ?)";
			stmt = connection.prepareStatement(createEntry, Statement.RETURN_GENERATED_KEYS);
			LocalDate checkoutDate = LocalDate.now();
			LocalDate dueDate = checkoutDate.plusDays(checkoutLimit);
			stmt.setString(1, userId);
			stmt.setInt(2, copyId);
			stmt.setDate(3, Date.valueOf(checkoutDate));
			stmt.setDate(4, Date.valueOf(dueDate));
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				entry = new CheckoutEntry(rs.getInt(1), userId, copyId, checkoutDate, dueDate);
			}
			stmt.close();
			rs.close();
			
			// update book copy
			String updateCopy = "UPDATE book_copy SET available = 0 WHERE copy_id = ?";
			stmt = connection.prepareStatement(updateCopy);
			stmt.setInt(1, copyId);
			stmt.executeUpdate();
			stmt.close();
			rs.close();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return entry;
	}
	
	@Override
	public List<CheckoutEntry> getUserCheckoutLog(String userId) {
		List<CheckoutEntry> logs = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String query = "SELECT entry_id, user_id, copy_id, " + 
		           	   "       checkout_date, " + 
		           	   "       due_date, " +
		           	   "       return_date, " + 
		           	   "       status " +
		           	   " FROM checkout_entry " +
		           	   " WHERE user_id = ?";
			stmt = connection.prepareStatement(query);
			stmt.setString(1, userId);
			rs = stmt.executeQuery();

			while (rs.next()) {
				Date checkoutDate = rs.getDate("checkout_date");
				Date dueDate = rs.getDate("due_date");
				Date returnDate =  rs.getDate("return_date");
				logs.add(new CheckoutEntry(rs.getInt("entry_id"),
						 rs.getString("user_id"), 
						 rs.getInt("copy_id"), 
						 (checkoutDate != null) ? checkoutDate.toLocalDate() : null, 
						 (dueDate != null) ? dueDate.toLocalDate() : null,
						 (returnDate != null) ? returnDate.toLocalDate() : null,
						 rs.getString("status")));
 
			}
			stmt.close();
			rs.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return logs;
	}
}
