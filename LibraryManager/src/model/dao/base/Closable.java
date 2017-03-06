package model.dao.base;

public interface Closable {
	public void onLoad();
	public void onClose();
}
