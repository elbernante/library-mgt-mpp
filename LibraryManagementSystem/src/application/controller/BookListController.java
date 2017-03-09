package application.controller;

import application.dao.base.DaoSession;
import application.model.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
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
		loadWindow("scene/BookAdd.fxml", "Add Book", false);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
		titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
		authorsCol.setCellValueFactory(new PropertyValueFactory<>("authorNames"));

		List<Book> books = DaoSession.getDb().findAllBooks();
		bookTable.getItems().setAll(books);
	}
}
