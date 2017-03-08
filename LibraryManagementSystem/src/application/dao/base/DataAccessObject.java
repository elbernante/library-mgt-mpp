package application.dao.base;

public interface DataAccessObject extends Closable {
	public void getPerson();
	public boolean authenticate(String username, String hash);
}
