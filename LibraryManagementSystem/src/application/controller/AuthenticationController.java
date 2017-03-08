package application.controller;

import application.util.WindowUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AuthenticationController {

	@FXML
	private JFXTextField username;

	@FXML
	private JFXPasswordField password;

	@FXML
	private JFXButton signInButton;

	@FXML
	private Pane authenticationForm;

	@FXML
	void signIn(ActionEvent event) {
		System.out.println(username.getText() + " : " + password.getText());
		// TODO: check username and password
		close();
		WindowUtil.loadWindow("scene/Main.fxml", "Login", false);
	}

	void close() {
		Stage stage = (Stage) authenticationForm.getScene().getWindow();
		stage.close();
	}

}
