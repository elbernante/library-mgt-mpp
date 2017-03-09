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
		loadWindow("scene/Member.fxml", "Add Member", false, (loader) -> {
			MemberController controller = loader.<MemberController>getController();
			controller.onSave((user) -> {
				System.out.println("User saved: " + user.getUserId());
			});
//			controller.setName("Trong");
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}
