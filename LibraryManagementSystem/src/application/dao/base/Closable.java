package application.dao.base;

public interface Closable {
	public void onLoad();

	public void onClose();
}
