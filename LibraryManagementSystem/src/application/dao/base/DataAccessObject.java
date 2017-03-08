package application.dao.base;

import model.*;

public interface DataAccessObject extends Closable {
	public void getPerson();
	public boolean authenticate(String username, String hash);
	public User getUserById(String userId);
}
