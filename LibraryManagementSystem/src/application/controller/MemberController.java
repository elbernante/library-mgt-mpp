package application.controller;


import java.net.URL;
import java.util.ResourceBundle;

import application.util.Constants;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import application.dao.base.DaoSession;
import application.model.User;
import application.util.WindowUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MemberController implements Initializable {

	public static interface OnSaveDelegate {
		public void onSave(User user);
	}

	private OnSaveDelegate onSaveDelegate = null;

	private User user = null;

	@FXML
    private BorderPane memberForm;

    @FXML
    private JFXTextField memberId;

    @FXML
    private JFXTextField firstName;

    @FXML
    private JFXTextField lastName;

    @FXML
    private JFXTextField street;

    @FXML
    private JFXTextField city;

    @FXML
    private JFXComboBox<String> state;

    @FXML
    private JFXTextField zip;

    @FXML
    private JFXTextField phone;

    @FXML
    private JFXButton okButton;

    @FXML
    private JFXButton cancelButton;

    @FXML
    void cancelAction(ActionEvent event) {
    	close();
    }

    @FXML
    void okAction(ActionEvent event) {
    	readFields();
    	if (!DaoSession.getDb().saveNewUser(user)) {
    		WindowUtil.messageBox("Error saving new user!");
    	} else {
    		close();
    		if (onSaveDelegate != null) onSaveDelegate.onSave(user);
    	}
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		state.getItems().addAll(Constants.STATE_CODES);
	}

	public void setData(User data) {
		this.user = data;
		memberId.setText(user.getUserId());
		firstName.setText(user.getFirstName());
		lastName.setText(user.getLastName());
		street.setText(user.getAddress().getStreet());
		city.setText(user.getAddress().getCity());
		state.setValue(user.getAddress().getState());
		zip.setText(user.getAddress().getZip());
		phone.setText(user.getAddress().getPhone());
	}

	public void onSave(OnSaveDelegate delegate) {
		this.onSaveDelegate = delegate;
	}

	private void close() {
		((Stage) memberForm.getScene().getWindow()).close();
	}

	private void readFields() {
		if (user == null) {
			user = new User();
			user.setPassword("");
		}
		user.setUserId(memberId.getText().toLowerCase());
		user.setFirstName(firstName.getText());
		user.setLastName(lastName.getText());
		user.getAddress().setStreet(street.getText());
		user.getAddress().setCity(city.getText());
		user.getAddress().setState(state.getValue());
		user.getAddress().setZip(zip.getText());
		user.getAddress().setPhone(phone.getText());
	}
}
