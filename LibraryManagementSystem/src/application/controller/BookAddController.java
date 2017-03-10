package application.controller;

import application.dao.base.DaoSession;
import application.dao.base.DataAccessObject;
import application.model.Author;
import application.model.Book;
import application.model.BookCopy;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import static application.util.WindowUtil.loadWindow;
import static application.util.WindowUtil.showErrorMessage;

public class BookAddController implements Initializable {
	private DataAccessObject db;

	public static interface OnSaveDelegate {
		public void onSave(Book book);
	}

	private OnSaveDelegate onSaveDelegate = null;
	private boolean isEditing = false;

	@FXML
	Pane bookAddForm;

	@FXML
	JFXTextField bookIsbn;

	@FXML
	JFXTextField bookTitle;

	@FXML
	JFXTextField bookAuthors;

	@FXML
	JFXTextField bookNumOfCopies;

	@FXML
	private void cancel() {
		close();
	}

	@FXML
	private void createNewBook() {
		try {
			String isbn = bookIsbn.getText();
			String title = bookTitle.getText();
			int numOfCopies = bookNumOfCopies.getText().equals("") ? 1 : Integer.parseInt(bookNumOfCopies.getText());
			List<Author> authors = (List<Author>) bookAuthors.getUserData();

			if (isbn.isEmpty()) {
				showErrorMessage("Please enter book ISBN");
				return;
			}

			if (title.isEmpty()) {
				showErrorMessage("Please enter book title");
				return;
			}

			if (!this.isEditing && numOfCopies <= 0) {
				showErrorMessage("Sorry, the book must have at least 1 copy.");
				return;
			}

			Book existingBook = db.findBookByIsbn(isbn);

			if (this.isEditing) {
				existingBook.setTitle(title);
				existingBook.setAuthors(authors);
				db.updateBook(existingBook);
			} else {
				if (existingBook != null) {
					showErrorMessage("Sorry, we cannot add your new book as the ISBN " + isbn + " already exists in our system.");
					return;
				}

				Book newBook = new Book(isbn, title);
				newBook.setAuthors(authors);

				db.createBook(newBook);

				for (int i = 1; i <= numOfCopies; i++) {
					BookCopy bookCopy = new BookCopy(i, true);
					bookCopy.setBook(newBook);
					db.createBookCopy(bookCopy);
				}
			}
			if (onSaveDelegate != null) {
				onSaveDelegate.onSave(null);
			}
			close();
		} catch (SQLException e) {
			showErrorMessage("Sorry, there was an error while creating your book.\n" + e.getMessage());
			e.printStackTrace();
		}
	}

	@FXML
	private void chooseAuthors() {
		loadWindow("scene/AuthorList.fxml", "Choose Authors", true, true, loader -> {
			AuthorListController controller = loader.<AuthorListController>getController();
			controller.toggleEditMode(true);
			controller.onSave(authors -> {
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
				bookAuthors.setText(sb.toString());
				bookAuthors.setUserData(authors);
			});
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		db = DaoSession.getDb();
	}

	private void close() {
		Stage stage = (Stage) bookAddForm.getScene().getWindow();
		stage.close();
	}

	public void onSave(OnSaveDelegate delegate) {
		this.onSaveDelegate = delegate;
	}

	public void setData(Book data) {
		try {
			this.isEditing = true;

			bookIsbn.setText(data.getIsbn());
			bookIsbn.setDisable(true);

			bookTitle.setText(data.getTitle());

			bookNumOfCopies.setText(String.valueOf(db.findCopiesByIsbn(data.getIsbn()).size()));
			bookNumOfCopies.setDisable(true);

			bookAuthors.setText(data.getAuthorNames());
			bookAuthors.setUserData(data.getAuthors());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
