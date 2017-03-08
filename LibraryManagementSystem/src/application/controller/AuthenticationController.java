package application.controller;

import application.Session;
import application.dao.base.DaoSession;
import application.util.Hash;
import application.util.WindowUtil;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
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
    private Label welcome;
	
	@FXML
    private Label errorMessage;

	@FXML
	void signIn(ActionEvent event) {
		String userId = username.getText();
		String passwordHash = Hash.md5(password.getText());
		
		showErrorMessage(false);
		if (DaoSession.getDb().authenticate(userId, passwordHash)) {
    		close();
    		Session.setCurrentUser(userId);
    		WindowUtil.loadWindow("scene/Main.fxml", "Login", false);
    	} else {
    		showErrorMessage(true);
    		username.requestFocus();
    	}
	}
	
	@FXML
	void showWelcome(KeyEvent event) {
		if (!welcome.isVisible()) showErrorMessage(false);
	}

	void close() {
		Stage stage = (Stage) authenticationForm.getScene().getWindow();
		stage.close();
	}
	
	private void showErrorMessage(boolean show){
		welcome.setVisible(!show);
		errorMessage.setVisible(show);
	}
}
