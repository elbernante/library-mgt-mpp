package application.model;

import java.util.ArrayList;
import java.util.List;

public class Book {
	private String isbn;
	private String title;
	private List<Author> authors;
	private List<BookCopy> copies;

	public Book(String isbn, String title) {
		this.isbn = isbn;
		this.title = title;
		this.authors = new ArrayList<>();
		this.copies = new ArrayList<>();
	}

	public boolean isAvailable() {
		for (BookCopy bookCopy : copies) {
			if (bookCopy.isAvailable()) {
				return true;
			}
		}
		return false;
	}

	public void addAuthor(Author author) {
		this.authors.add(author);
	}

	public void addCopy(BookCopy bookCopy) {
		bookCopy.setBook(this);
		this.copies.add(bookCopy);
	}

	public String getAuthorNames() {
		StringBuilder sb = new StringBuilder();
		int size = authors.size();
		for (int i = 0; i < size; i++) {
			Author author = authors.get(i);
			sb.append(author.getFirstName());
			sb.append(" ");
			sb.append(author.getLastName());
			if (i < size - 1) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public List<BookCopy> getCopies() {
		return copies;
	}

	public void setCopies(List<BookCopy> copies) {
		this.copies = copies;
	}
}
