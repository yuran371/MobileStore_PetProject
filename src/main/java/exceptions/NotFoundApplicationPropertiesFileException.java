package exceptions;

import java.io.IOException;

public class NotFoundApplicationPropertiesFileException extends RuntimeException{
	private final static String errorMessage = "File application.properties not found or file cannot be read";
	public NotFoundApplicationPropertiesFileException(Throwable e) {
		super(errorMessage, e);
	}
}
