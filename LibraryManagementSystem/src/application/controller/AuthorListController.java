package application.controller;

import application.dao.base.DaoSession;
import application.model.Author;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import static application.util.WindowUtil.loadWindow;
import static application.util.WindowUtil.showErrorMessage;

public class AuthorListController implements Initializable {
	public static interface OnSaveDelegate {
		public void onSave(List<Author> author);
	}

	private OnSaveDelegate onSaveDelegate = null;

	@FXML
	private Pane actionPane;

	@FXML
	private Pane authorForm;

	@FXML
	private TableView<Author> authorTable;

	@FXML
	private TableColumn<Author, Integer> idCol;

	@FXML
	private TableColumn<Author, String> firstNameCol;

	@FXML
	private TableColumn<Author, String> lastNameCol;

	@FXML
	private TableColumn<Author, String> streetCol;

	@FXML
	private TableColumn<Author, String> cityCol;

	@FXML
	private TableColumn<Author, String> stateCol;

	@FXML
	private TableColumn<Author, String> zipCol;

	@FXML
	private TableColumn<Author, String> phoneCol;

	@FXML
	private TableColumn<Author, String> credentialsCol;

	@FXML
	private TableColumn<Author, String> bioCol;

	@FXML
	private void openAuthorAddWindow(ActionEvent event) {
		showAddAuthorForm(null);
	}

	@FXML
	private void handleMousePressed(MouseEvent event) {
		if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
			Author selectedAuthor = authorTable.getSelectionModel().getSelectedItem();
			showAddAuthorForm(selectedAuthor);
		}
	}

	@FXML
	private void close() {
		Stage stage = (Stage) authorForm.getScene().getWindow();
		stage.close();
	}

	@FXML
	private void handleOkAction() {
		ObservableList<Author> selectedItems = authorTable.getSelectionModel().getSelectedItems();
		if (selectedItems.size() == 0) {
			showErrorMessage("Please select at least one author.");
		} else if (onSaveDelegate != null) {
			onSaveDelegate.onSave(selectedItems);
			close();
		}
	}

	private void showAddAuthorForm(Author selectedAuthor) {
		String formTitle = selectedAuthor == null ? "Create Author" : "Edit Author";
		loadWindow("scene/AuthorAdd.fxml", formTitle, false, true, (loader -> {
			AuthorAddController controller = loader.<AuthorAddController>getController();
			if (selectedAuthor != null) {
				controller.setData(selectedAuthor);
			}
			controller.onSave(author -> {
				loadAuthors();
			});
		}));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		streetCol.setCellValueFactory(new PropertyValueFactory<>("street"));
		cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
		stateCol.setCellValueFactory(new PropertyValueFactory<>("state"));
		zipCol.setCellValueFactory(new PropertyValueFactory<>("zip"));
		phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
		credentialsCol.setCellValueFactory(new PropertyValueFactory<>("credentials"));
		bioCol.setCellValueFactory(new PropertyValueFactory<>("shortBio"));

		loadAuthors();
	}

	private void loadAuthors() {
		List<Author> authors = null;
		try {
			authors = DaoSession.getDb().findAllAuthors();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		authorTable.getItems().setAll(authors);
		authorTable.refresh();
	}

	public void toggleEditMode(boolean editMode) {
		if (editMode) {
			authorTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			actionPane.setVisible(true);
		} else {
			authorTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			actionPane.setVisible(false);
		}
	}

	public void onSave(OnSaveDelegate delegate) {
		this.onSaveDelegate = delegate;
	}
}
