package application.model;

public class BookCopy {
	private Integer copyId;
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

	public Book getBook() {
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
