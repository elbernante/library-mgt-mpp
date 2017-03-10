package application.dao.base;

import java.util.List;
import java.sql.SQLException;
import application.model.*;

public interface DataAccessObject extends Closable {
	public void getPerson();

	public boolean authenticate(String username, String hash);

	public User getUserById(String userId);

	public List<User> findAllUsers();

	public boolean saveNewUser(User user);

	public List<Book> findAllBooks() throws SQLException;

	public Book findBookByIsbn(String isbn) throws SQLException;
	
	public Book getBookByIsbn(String isbn);
	
	public BookCopy getBookCopyById(int id);

	public List<Author> findAuthorsByIsbn(String isbn) throws SQLException;

	public List<BookCopy> findCopiesByIsbn(String isbn) throws SQLException;

	public void createBook(Book book) throws SQLException;

	public void updateBook(Book book) throws SQLException;

	public void createBookCopy(BookCopy bookCopy) throws SQLException;
	
	public CheckoutEntry checkoutCopy(String userId, int copyId);
	
	public List<CheckoutEntry> getUserCheckoutLog(String userId);
}
