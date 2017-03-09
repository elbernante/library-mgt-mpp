package application.controller;

import application.dao.base.DaoSession;
import application.model.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import static application.util.WindowUtil.loadWindow;

public class BookListController implements Initializable {
	@FXML
	private TableView<Book> bookTable;

	@FXML
	private TableColumn<Book, String> isbnCol;

	@FXML
	private TableColumn<Book, String> titleCol;

	@FXML
	private TableColumn<Book, String> authorsCol;

	@FXML
	private void openBookAddWindow(ActionEvent event) {
		showAddBookForm(null);
	}

	@FXML
	private void handleMousePressed(MouseEvent event) {
		if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
			Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
			showAddBookForm(selectedBook);
		}
	}

	private void showAddBookForm(Book selectedBook) {
		loadWindow("scene/BookAdd.fxml", "Edit Book", false, true, (loader -> {
			BookAddController controller = loader.<BookAddController>getController();
			if (selectedBook != null) {
				controller.setData(selectedBook);
			}
			controller.onSave((book -> {
				loadBooks();
			}));
		}));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
		titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
		authorsCol.setCellValueFactory(new PropertyValueFactory<>("authorNames"));

		loadBooks();
	}

	private void loadBooks() {
		List<Book> books = null;
		try {
			books = DaoSession.getDb().findAllBooks();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		bookTable.getItems().setAll(books);
		bookTable.refresh();
	}
}
