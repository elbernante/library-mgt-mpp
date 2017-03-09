package application.dao.base;

import java.util.List;

import application.model.*;

public interface DataAccessObject extends Closable {
	public void getPerson();

	public boolean authenticate(String username, String hash);

	public User getUserById(String userId);
	
	public boolean saveNewUser(User user);

	public List<Book> findAllBooks();

	public List<Author> findAuthorsByIsbn(String isbn);

	public List<BookCopy> findCopiesByIsbn(String isbn);


}
