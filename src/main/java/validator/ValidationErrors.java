package validator;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrors {

	private static ValidationErrors INSTANCE = new ValidationErrors();
	private static List<Error> CreateAccountErrors = new ArrayList<>();

	private ValidationErrors() {
	}

	public static ValidationErrors getInstance() {
		return INSTANCE;
	}

	public boolean addCreateAccountError(Error error) {
		try {
			return CreateAccountErrors.add(error);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<Error> getCreateAccountErrors() {
		return CreateAccountErrors;
	}

}