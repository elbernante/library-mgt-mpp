package application.dao.base;

import application.dao.SqliteDao;

public class DaoSession {
	public static enum DbType {SQLITE}

	;

	private static DaoSession instance;
	private DataAccessObject db;

	private DaoSession(DbType dbType, String url) {
		switch (dbType) {
			case SQLITE:
				db = SqliteDao.getInstance(url);
				db.onLoad();
				break;

			default:
				throw new RuntimeException("Unsupported data storage");
		}
	}

	public static void start(DbType dbType, String url) {
		if (instance != null) return;
		instance = new DaoSession(dbType, url);
	}

	public static void stop() {
		if (instance == null) return;
		instance.db.onClose();
	}

	public static DataAccessObject getDb() {
		if (instance == null) throw new RuntimeException("DAO session has not started.");
		return instance.db;
	}
}
