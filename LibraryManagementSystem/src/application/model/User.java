package application.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import application.dao.base.DaoSession;

public class User {
	private String userId;
	private String password;
	private String firstName;
	private String lastName;
	private Set<Integer> roles;
	private UserAddress address;
	private List<CheckoutEntry> checkoutLog;
	
	public User() {
		this.roles = new HashSet<>();
		this.address = new UserAddress();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Set<Integer> getRoles() {
		return roles;
	}

	public void addRole(int roleId) {
		roles.add(roleId);
	}
	
	public UserAddress getAddress() {
		return address;
	}

	public void setAddress(UserAddress address) {
		this.address = address;
	}
	
	public List<CheckoutEntry> getCheckoutLog() {
		loadCheckoutLogs();
		return checkoutLog;
	}
	
	public void addCheckoutEntry(CheckoutEntry entry) {
		loadCheckoutLogs();
		checkoutLog.add(entry);
	}
	
	private void loadCheckoutLogs() {
		if (checkoutLog != null) return;
		checkoutLog = DaoSession.getDb().getUserCheckoutLog(userId);
	}

	public boolean isAdmin() {
		for (int i : roles) if (i == 0) return true;
		return false;
	}
	
	public boolean isLibrarian() {
		for (int i : roles) if (i == 1) return true;
		return false;
	}
	
}
