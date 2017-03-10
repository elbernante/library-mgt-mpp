package application.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import application.dao.base.DaoSession;
import application.model.Book;
import application.model.BookCopy;
import application.model.CheckoutEntry;
import application.model.User;
import application.util.WindowUtil;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
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
    private TableColumn<BookCopy, BookCopy> cpActionColumn;
    
    @FXML
    public void initialize() {
    	cpCopyIdColumn.setCellValueFactory(new PropertyValueFactory<>("copyId"));
    	cpIsAvailableColumn.setCellValueFactory((p) -> {
    		return new ReadOnlyStringWrapper(p.getValue().isAvailable() ? "Available" : "Checked out");
    	});
    	
    	
    	// Action column
    	cpActionColumn.setCellFactory((p) -> {
    		return new CheckoutCell(bookCopy -> this.checkoutCopy(bookCopy));
    	});
    	
    	cpActionColumn.setCellValueFactory((p) -> {
    		return new ReadOnlyObjectWrapper<BookCopy>(p.getValue());
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
    		// TODO: if no copies available, show message
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
    
    private void checkoutCopy(BookCopy bookCopy) {
    	
    		if (member == null) {
    			WindowUtil.messageBox("Select a member to checkout the book");
    			memberIdField.requestFocus();
    			return;
    		}
    		
    		CheckoutEntry entry = DaoSession.getDb().checkoutCopy(member.getUserId(), bookCopy.getCopyId());
    		if (entry == null) {
    			WindowUtil.messageBox("There was an error checking out the book.");
    			return;
    		}
    		
    		// TODO: add entry to user checkout log
    		// TODO: refresh checkout log table
    		
    		// update current copy
 			bookCopy.setAvailable(false);
 			
 			// refresh UIs
 			
 			// dirty hack to refresh table
 			copiesTable.getColumns().get(0).setVisible(false);
 			copiesTable.getColumns().get(0).setVisible(true);
    }
    
    private void clearMemeberInfo() {
    	nameLabel.setText("-");
    	memberIdLabel.setText("-");
    	checkoutTable.getItems().setAll();
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
    
    private static interface  ActionDelegate <T> {
    	public void onAction(T t);
    }

    private static class CheckoutCell extends TableCell<BookCopy, BookCopy>{
    	private JFXButton button;
    	private ActionDelegate<BookCopy> delegate;
    	
    	public CheckoutCell(ActionDelegate<BookCopy> delegate) {
    		this.delegate = delegate;
			button = new JFXButton("Checkout");
			button.setStyle("fx-padding: 0.1em 0.1em; " +
							"-fx-font-size: 13px; " +
							"-jfx-button-type: RAISED; " +
							"-fx-background-color: rgb(10, 206, 43); " +
							"-fx-pref-width: 200; " +
							"-fx-text-fill: WHITE;");
			button.setOnAction((event) -> {
				if (delegate != null) delegate.onAction(getItem());
			});
		}
    	
    	@Override
    	protected void updateItem(BookCopy bookCopy, boolean empty) {
            super.updateItem(bookCopy, empty);
            if (empty || !bookCopy.isAvailable()) {
            	setGraphic(null);
            } else {
            	button.setText("Checkout");
            	setGraphic(button);
            }
    	}
    }
}