package pureJavaTests;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import utlis.ConnectionPoolManager;
import utlis.PropertiesUtil;

public class TestUtils {

	public static void main(String[] args) {
		PropertiesUtil.getInstance();
		try (Connection connection = ConnectionPoolManager.get();
				Statement createStatement = connection.createStatement()) {
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
