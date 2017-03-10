package application.model;

import application.dao.base.DaoSession;

public class BookCopy {
	private Integer copyId;
	private String isbn;
	private Book book;
	private boolean available;

	public BookCopy(Integer copyId, boolean available) {
		this.copyId = copyId;
		this.available = available;
	}

	public Integer getCopyId() {
		return copyId;
	}

	public void setCopyId(Integer copyId) {
		this.copyId = copyId;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Book getBook() {
		if (book == null && isbn !=null) {
			book = DaoSession.getDb().getBookByIsbn(isbn);
		}
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}
}
