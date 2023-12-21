package validator;

import java.util.ArrayList;
import java.util.List;

import lombok.Singleton;

@Singleton(style = Singleton.Style.HOLDER)
public class ValidationErrors {

	private static List<Error> CreateAccountErrors = new ArrayList<>();

	public boolean addCreateAccountError(Error error) {
		try {
			return CreateAccountErrors.add(error);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static List<Error> getCreateAccountErrors() {
		return CreateAccountErrors;
	}
}