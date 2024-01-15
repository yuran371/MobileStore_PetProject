package pureJavaTests;
import utlis.ConnectionPoolManager;
import utlis.PropertiesUtil;

public class ExceptionsTest {
	public static void main(String[] args) {
		System.out.println(PropertiesUtil.getInstance().getProperty("db.pool.size"));
		ConnectionPoolManager.get();
	}
}
