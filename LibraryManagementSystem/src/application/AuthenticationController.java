package application;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import application.dao.base.DaoSession;
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
    	
    	if (DaoSession.getDb().authenticate(username.getText(), md5(password.getText()))) {
    		System.out.println("Success");
    	} else {
    		System.out.println("Failed");
    	}
    }
    
    public static String md5(String input) {
		
		String md5 = null;
		
		if(null == input) return null;
		
		try {
			//Create MessageDigest object for MD5
			MessageDigest digest = MessageDigest.getInstance("MD5");
			
			//Update input string in message digest
			digest.update(input.getBytes(), 0, input.length());
	
			//Converts message digest value in base 16 (hex) 
			md5 = new BigInteger(1, digest.digest()).toString(16);

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
		return md5;
	}

}
