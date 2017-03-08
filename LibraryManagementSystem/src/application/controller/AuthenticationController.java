package application.controller;

import application.dao.base.DaoSession;
import application.util.Hash;
import application.util.WindowUtil;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
		showErrorMessage(false);
		if (DaoSession.getDb().authenticate(username.getText(), Hash.md5(password.getText()))) {
    		// TODO: Set current session user
    		close();
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
