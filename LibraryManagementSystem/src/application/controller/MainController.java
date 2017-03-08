package application.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import static application.util.WindowUtil.loadWindow;

public class MainController implements Initializable {

	@FXML
	private void openBookListWindow(ActionEvent event) {
		loadWindow("scene/BookList.fxml", "Manage Books", true);
	}

	@FXML
	private void openMemberListWindow(ActionEvent event) {
		loadWindow("scene/MemberList.fxml", "Manage Members", true);
	}

	@FXML
	private void openCheckoutWindow(ActionEvent event) {
		loadWindow("scene/CheckoutForm.fxml", "Checkout Book", true);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}
