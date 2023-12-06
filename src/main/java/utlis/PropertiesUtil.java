package utlis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesUtil {

	private final static PropertiesUtil INSTANCE = new PropertiesUtil();

	private PropertiesUtil() {
	}

	public static PropertiesUtil getInstance() {
		return INSTANCE;
	}

	public String getProperty(String property) {
		Properties prop = new Properties();
		try (InputStream resourceAsStream = PropertiesUtil.class.getClassLoader()
				.getResourceAsStream("application.properties")) {
			prop.load(resourceAsStream);
			return prop.getProperty(property);
		} catch (IOException e) {
			// TODO throw new NotFoundApplicationPropertiesFileException();
			throw new RuntimeException(e);
		}
	}
}
