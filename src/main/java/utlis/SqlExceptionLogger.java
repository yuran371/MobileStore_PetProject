package utlis;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlExceptionLogger {

	private static List<SQLException> sqlExeptionList = new ArrayList<>();

	private SqlExceptionLogger() {
	}

	private static class SingletonHelper {
		private static final SqlExceptionLogger INSTANCE = new SqlExceptionLogger();
	}

	public static SqlExceptionLogger getInstance() {
		return SingletonHelper.INSTANCE;
	}

	public boolean addException(SQLException e) {
		try {
			return sqlExeptionList.add(e);
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
	}
}
