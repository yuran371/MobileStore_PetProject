package utlis;

import exceptions.NotFoundApplicationPropertiesFileException;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@UtilityClass
public final class PropertiesUtil {

	public String getProperty(String property) {
		Properties prop = new Properties();
		try (InputStream resourceAsStream = PropertiesUtil.class.getClassLoader()
				.getResourceAsStream("application.properties")) {
			prop.load(resourceAsStream);
			return prop.getProperty(property);
		} catch (NullPointerException | IOException e) {
			throw new NotFoundApplicationPropertiesFileException(e);
		}
	}
}
