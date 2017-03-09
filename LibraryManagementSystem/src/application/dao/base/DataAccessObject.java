package application.dao.base;

import application.model.Author;
import application.model.Book;
import application.model.BookCopy;
import application.model.User;

import java.util.List;

public interface DataAccessObject extends Closable {
	public void getPerson();

	public boolean authenticate(String username, String hash);

	public User getUserById(String userId);

	public List<Book> findAllBooks();

	public List<Author> findAuthorsByIsbn(String isbn);

	public List<BookCopy> findCopiesByIsbn(String isbn);
}
