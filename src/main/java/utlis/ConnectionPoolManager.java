package utlis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;

import exceptions.DriverClassLoadException;

public final class ConnectionPoolManager {

	private final static String SQL_USER_KEY = "db.user";
	private final static String SQL_PASSWORD_KEY = "db.password";
	private final static String SQL_URL_KEY = "db.url";
	private final static String SQL_CONNECTION_POOL_SIZE = "db.pool.size";
	private static ArrayBlockingQueue<Connection> connectionPool;

	static {
		load();
		createConnectionPool();
	}

	private static void createConnectionPool() {
		String poolSize = PropertiesUtil.getInstance().getProperty(SQL_CONNECTION_POOL_SIZE);
		int size = poolSize == null ? 10 : Integer.parseInt(poolSize);
		connectionPool = new ArrayBlockingQueue<>(size);
		for (int i = 0; i < size; i++) {
			connectionPool.add(open());
		}
	}

	private static void load() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			throw new DriverClassLoadException(e);
		}
	}

	private static Connection open() {
		try {
			return DriverManager.getConnection(PropertiesUtil.getInstance().getProperty(SQL_URL_KEY),
					PropertiesUtil.getInstance().getProperty(SQL_USER_KEY),
					PropertiesUtil.getInstance().getProperty(SQL_PASSWORD_KEY));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static Connection get() {
		try {
			return connectionPool.take();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
