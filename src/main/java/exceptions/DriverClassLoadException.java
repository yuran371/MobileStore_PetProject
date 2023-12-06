package exceptions;

public class DriverClassLoadException extends RuntimeException {
	
	private final static String errorMessage = "org.postgresql.Driver cannot be found or load";

	public DriverClassLoadException(Throwable cause) {
		super(errorMessage, cause);
	}
	
}
