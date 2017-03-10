package application.controller;

import com.jfoenix.controls.JFXTextField;

import application.dao.base.DaoSession;
import application.model.Book;
import application.model.BookCopy;
import application.model.CheckoutEntry;
import application.model.User;
import application.util.WindowUtil;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CheckoutFormController {
	
	User member = null;
	Book book = null;

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
    private Label checkoutLimitLabel;

    @FXML
    private TableView<BookCopy> copiesTable;

    @FXML
    private TableColumn<BookCopy, String> cpCopyIdColumn;

    @FXML
    private TableColumn<BookCopy, String> cpIsAvailableColumn;

    @FXML
    private TableColumn<BookCopy, String> cpActionColumn;
    
    @FXML
    public void initialize() {
    	cpCopyIdColumn.setCellValueFactory(new PropertyValueFactory<>("copyId"));
    	cpIsAvailableColumn.setCellValueFactory((p) -> {
    		return new ReadOnlyStringWrapper(p.getValue().isAvailable() ? "Available" : "Checked out");
    	});
    }

    @FXML
    void searchMemberIdAction(ActionEvent event) {    	
    	String query = memberIdField.getText().toLowerCase();
    	User u = DaoSession.getDb().getUserById(query);
    	setMember(u);
    	if (u == null) {
    		WindowUtil.messageBox("User ID '" + query + "' not found.");
    		memberIdField.selectAll();
    	} else {
    		isbnField.requestFocus();
    	}
    }
    
    @FXML
    void searchIsbnAction(ActionEvent event) {
    	String query = isbnField.getText();
    	Book b = DaoSession.getDb().getBookByIsbn(query);
    	setBook(b);
    	if (b == null) {
    		WindowUtil.messageBox("ISBN '" + query + "' not found.");
    		isbnField.selectAll();
    	} else {
    		copiesTable.requestFocus();
    	}
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
    	nameLabel.setText("-");
    	memberIdLabel.setText("-");
    	// TODO: clear checkout table
    }
    
    public void setBook(Book book) {
    	this.book = book;
    	if (book == null) {
    		clearBookInfo();
    	} else {
    		bookTitleLabel.setText(book.getTitle());
        	isbnLabel.setText(book.getIsbn());
        	authorsLabel.setText(book.getAuthorNames());
        	checkoutLimitLabel.setText(book.getCheckoutLimit() + " days");
        	copiesTable.getItems().setAll(book.getCopies());
    	}
    }
    
    private void clearBookInfo() {
    	bookTitleLabel.setText("-");
    	isbnLabel.setText("-");
    	authorsLabel.setText("-");
    	checkoutLimitLabel.setText("-");
    	copiesTable.getItems().setAll();
    }
    

}
