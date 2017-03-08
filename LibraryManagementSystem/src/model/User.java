package model;

import java.util.HashSet;
import java.util.Set;

public class User {
	private String userId;
	private String password;
	private String firstName;
	private String lastName;
	private Set<Integer> roles;
	
	public User() {
		this.roles = new HashSet<>();
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
	
	public boolean isAdmin() {
		for (int i : roles) if (i == 0) return true;
		return false;
	}
	
	public boolean isLibrarian() {
		for (int i : roles) if (i == 1) return true;
		return false;
	}
}
