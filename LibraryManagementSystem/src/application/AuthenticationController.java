package application;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AuthenticationController {

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXButton signInButton;

    @FXML
    void signIn(ActionEvent event) {
    	System.out.println(username.getText() + " : " + password.getText());
    }

}
