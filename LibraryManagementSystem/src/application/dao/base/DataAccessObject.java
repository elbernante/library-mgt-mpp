package application.dao.base;

import application.model.*;

public interface DataAccessObject extends Closable {
	public void getPerson();
	public boolean authenticate(String username, String hash);
	public User getUserById(String userId);
	public boolean saveNewUser(User user);
}
