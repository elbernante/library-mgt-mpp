package application.controller;

import com.jfoenix.controls.JFXTextField;

import application.dao.base.DaoSession;
import application.model.BookCopy;
import application.model.CheckoutEntry;
import application.model.User;
import application.util.WindowUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class CheckoutFormController {
	
	User member = null;

    @FXML
    private JFXTextField memberIdField;

    @FXML
    private Label nameLabel;

    @FXML
    private Label memberIdLabel;

    @FXML
    private TableView<CheckoutEntry> checkoutTable;

    @FXML
    private TableColumn<CheckoutEntry, String> cktIsbnColumn;

    @FXML
    private TableColumn<CheckoutEntry, String> cktCopyIdColumn;

    @FXML
    private TableColumn<CheckoutEntry, String> cktTitleColumn;

    @FXML
    private TableColumn<CheckoutEntry, String> cktCheckoutDateCol;

    @FXML
    private TableColumn<CheckoutEntry, String> cktDueDateColumn;

    @FXML
    private JFXTextField isbnField;

    @FXML
    private Label bookTitleLabel;

    @FXML
    private Label isbnLabel;

    @FXML
    private Label authorsLabel;

    @FXML
    private TableView<BookCopy> copiesTable;

    @FXML
    private TableColumn<BookCopy, String> cpCopyIdColumn;

    @FXML
    private TableColumn<BookCopy, String> cpIsAvailableColumn;

    @FXML
    private TableColumn<BookCopy, String> cpActionColumn;

    @FXML
    void searchMemberIdAction(ActionEvent event) {
    	
    	System.out.println(memberIdField.getText());
    	nameLabel.setText(memberIdField.getText());
    	
    	String query = memberIdField.getText().toLowerCase();
    	User u = DaoSession.getDb().getUserById(query);
    	setMember(u);
    	if (u == null) {
    		WindowUtil.messageBox("User ID " + query + " not found.");
    		memberIdField.selectAll();
    	} else {
    		isbnField.requestFocus();
    	}
    }
    
    @FXML
    void searchIsbnAction(ActionEvent event) {

    }

    
    @FXML
    void tempAction(ActionEvent event) {

    }
    
    public void setMember(User member) {
    	this.member = member;
    	if (member == null) {
    		clearMemeberInfo();
    	} else {
    		nameLabel.setText(member.getLastName() + ", " + member.getFirstName());
    		memberIdLabel.setText(member.getUserId());
    	}
    }
    
    private void clearMemeberInfo() {
    	nameLabel.setText("");
    	memberIdLabel.setText("");
    }
    

}
