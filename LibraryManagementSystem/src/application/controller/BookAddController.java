package application.controller;

import application.dao.base.DaoSession;
import application.dao.base.DataAccessObject;
import application.model.Book;
import application.model.BookCopy;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

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

			if (numOfCopies <= 0) {
				showError("Sorry, the book must have at least 1 copy.");
				return;
			}

			Book existingBook = db.findBookByIsbn(isbn);

			if (this.isEditing) {
				existingBook.setTitle(title);
				db.updateBook(existingBook);
			} else {
				if (existingBook != null) {
					showError("Sorry, we cannot add your new book as the ISBN " + isbn + " already exists in our system.");
					return;
				}

				Book newBook = new Book(isbn, title);
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
			showError("Sorry, there was an error while creating your book.\n" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		db = DaoSession.getDb();
	}

	private void showError(String msg) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
