package application.controller;

import application.dao.base.DaoSession;
import application.dao.base.DataAccessObject;
import application.model.Address;
import application.model.Author;
import application.util.Constants;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static application.util.WindowUtil.showErrorMessage;

public class AuthorAddController implements Initializable {
	public static interface OnSaveDelegate {
		void onSave(Author author);
	}

	private DataAccessObject db;
	private OnSaveDelegate onSaveDelegate = null;
	private Author editingAuthor = null;

	@FXML
	private Pane authorAddForm;

	@FXML
	private JFXTextField firstNameField;

	@FXML
	private JFXTextField lastNameField;

	@FXML
	private JFXTextField streetField;

	@FXML
	private JFXTextField cityField;

	@FXML
	private JFXComboBox<String> stateField;

	@FXML
	private JFXTextField zipField;

	@FXML
	private JFXTextField phoneField;

	@FXML
	private JFXTextField credentialsField;

	@FXML
	private JFXTextArea bioField;

	@FXML
	private void cancel() {
		close();
	}

	@FXML
	private void createNewAuthor() {
		try {
			String firstName = firstNameField.getText();
			String lastName = lastNameField.getText();
			String street = streetField.getText();
			String city = cityField.getText();
			String state = stateField.getValue();
			String zip = zipField.getText();
			String phone = phoneField.getText();
			String credentials = credentialsField.getText();
			String bio = bioField.getText();

			if (editingAuthor != null) {
				editingAuthor.setFirstName(firstName);
				editingAuthor.setLastName(lastName);
				editingAuthor.getAddress().setStreet(street);
				editingAuthor.getAddress().setCity(city);
				editingAuthor.getAddress().setState(state);
				editingAuthor.getAddress().setZip(zip);
				editingAuthor.setPhoneNumber(phone);
				editingAuthor.setCredentials(credentials);
				editingAuthor.setShortBio(bio);
				db.updateAuthor(editingAuthor);
			} else {
				Author newAuthor = new Author(firstName, lastName, new Address(street, city, state, zip), phone, credentials, bio);
				db.createAuthor(newAuthor);
			}
			if (onSaveDelegate != null) {
				onSaveDelegate.onSave(null);
			}
			close();
		} catch (SQLException e) {
			showErrorMessage("Sorry, there was an error while creating your author.\n" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		db = DaoSession.getDb();
		stateField.getItems().addAll(Constants.STATE_CODES);
	}

	private void close() {
		Stage stage = (Stage) authorAddForm.getScene().getWindow();
		stage.close();
	}

	public void onSave(OnSaveDelegate delegate) {
		this.onSaveDelegate = delegate;
	}

	public void setData(Author data) {
		this.editingAuthor = data;
		firstNameField.setText(data.getFirstName());
		lastNameField.setText(data.getLastName());
		streetField.setText(data.getStreet());
		cityField.setText(data.getCity());
		stateField.setValue(data.getState());
		zipField.setText(data.getZip());
		phoneField.setText(data.getPhoneNumber());
		credentialsField.setText(data.getCredentials());
		bioField.setText(data.getShortBio());
	}
}
