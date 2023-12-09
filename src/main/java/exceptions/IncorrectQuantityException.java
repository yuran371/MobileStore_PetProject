package exceptions;

public class IncorrectQuantityException extends RuntimeException {

	private final static String errorMessage = "Quantity is bigger then amount of items in Items table. Please change quantity";

	public IncorrectQuantityException() {
		super(errorMessage);
	}

}
