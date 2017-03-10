package application.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.Session;

import static application.util.WindowUtil.loadWindow;

public class MainController implements Initializable {
	
	@FXML
    private JFXButton checkoutButton;

	@FXML
	private void openBookListWindow(ActionEvent event) {
		loadWindow("scene/BookList.fxml", "Manage Books", true, true, null);
	}

	@FXML
	private void openMemberListWindow(ActionEvent event) {
		loadWindow("scene/MemberList.fxml", "Manage Members", true, true, null);
	}

	@FXML
	private void openCheckoutWindow(ActionEvent event) {
		loadWindow("scene/CheckoutForm.fxml", "Checkout Book", false, true, null);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		checkoutButton.setDisable(!Session.getCurrentUser().isLibrarian());
	}
}
