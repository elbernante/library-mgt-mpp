package application.model;

import java.time.LocalDate;

import application.dao.base.DaoSession;

public class CheckoutEntry {
	
	private int entryId;
	private String userId;
	private int copyId;
	private LocalDate checkoutDate;
	private LocalDate dueDate;
	private LocalDate returnDate;
	private String status;
	private BookCopy bookCopy;

	public CheckoutEntry () {
		this.entryId = -1;
		this.copyId = -1;
	}
	
	public CheckoutEntry(int entryId, String userId, int copyId, LocalDate checkoutDate, LocalDate dueDate) {
		this(entryId, userId, copyId, checkoutDate, dueDate, null, "OUT");
	}
	
	public CheckoutEntry(int entryId, String userId, int copyId, LocalDate checkoutDate, LocalDate dueDate, LocalDate returnDate, String status) {
		this.entryId = entryId;
		this.userId = userId;
		this.copyId = copyId;
		this.checkoutDate = checkoutDate;
		this.dueDate = dueDate;
		this.returnDate = returnDate;
		this.status = status;
	}

	public int getEntryId() {
		return entryId;
	}

	public void setEntryId(int entryId) {
		this.entryId = entryId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getCopyId() {
		return copyId;
	}

	public void setCopyId(int copyId) {
		this.copyId = copyId;
		this.bookCopy = null;
	}

	public LocalDate getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(LocalDate checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public BookCopy getBookCopy() {
		if (copyId < 0) return null;
		if (bookCopy == null) {
			bookCopy = DaoSession.getDb().getBookCopyById(copyId);
		}
		return bookCopy;
	}
}
