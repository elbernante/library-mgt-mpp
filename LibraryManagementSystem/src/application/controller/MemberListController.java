package application.controller;

import application.dao.base.DaoSession;
import application.model.User;
import application.model.UserAddress;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static application.util.WindowUtil.loadWindow;

public class MemberListController implements Initializable {

	@FXML
	private TableView<User> memberTable;

	@FXML
	private TableColumn<User, String> idCol;

	@FXML
	private TableColumn<User, String> firstNameCol;

	@FXML
	private TableColumn<User, String> lastNameCol;

	@FXML
	private TableColumn<User, String> addressCol;

	@FXML
	private TableColumn<User, String> phoneCol;

	@FXML
	private void openMemberAddWindow(ActionEvent event) {
		showMemberForm(null);
	}

	@FXML
	void handleMousePressed(MouseEvent event) {
		if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
			User selectedUser = memberTable.getSelectionModel().getSelectedItem();
			showMemberForm(selectedUser);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
		firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));

		addressCol.setCellValueFactory((param) -> {
			UserAddress a = param.getValue().getAddress();
			String city = a.getCity().trim();
			String state = a.getState().trim();
			String display = "";
			if (!city.equals("") && !state.equals("")) {
				display = city + ", " + state;
			} else if (city.equals("")) {
				display = state;
			} else if (state.equals("")) {
				display = city;
			}
			return new ReadOnlyStringWrapper(display);
		});

		phoneCol.setCellValueFactory((param) -> {
			return new ReadOnlyStringWrapper(param.getValue().getAddress().getPhone());
		});

		loadMembers();
	}

	private void loadMembers() {
		List<User> users = DaoSession.getDb().findAllUsers();
		memberTable.getItems().setAll(users);
	}

	private void showMemberForm(User targetUser) {
		loadWindow("scene/Member.fxml", targetUser != null ? "Edit Member" : "Create Member", false, true, (loader) -> {
			MemberController controller = loader.<MemberController>getController();
			if (targetUser != null) controller.setData(targetUser);
			controller.onSave((user) -> {
				System.out.println("User saved: " + user.getUserId());
				loadMembers();
			});
		});
	}
}
