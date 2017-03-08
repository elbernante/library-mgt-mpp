package application.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import static application.util.WindowUtil.loadWindow;

public class MemberListController implements Initializable {

	@FXML
	private void openMemberAddWindow(ActionEvent event) {
		loadWindow("scene/MemberAdd.fxml", "Add Member", false);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}
