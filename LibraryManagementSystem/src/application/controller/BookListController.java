package application.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import static application.util.WindowUtil.loadWindow;

public class BookListController implements Initializable {

	@FXML
	private void openBookAddWindow(ActionEvent event) {
		loadWindow("scene/BookAdd.fxml", "Add Book", false);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}
