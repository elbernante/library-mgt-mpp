package application;

import application.dao.base.DaoSession;
import model.User;

public class Session {
	
	private static User currentUser = null;
	
	private Session() {
		/* Prevent creating instances */
	}
	
	public static void setCurrentUser(String userId) {
		User user = DaoSession.getDb().getUserById(userId);
		if (user == null) {
			System.err.println("Unable to find user ID in the database.");
			currentUser = null;
		}
		currentUser = user;
	}
	
	public static User getCurrentUser() {
		return currentUser;
	}

}
