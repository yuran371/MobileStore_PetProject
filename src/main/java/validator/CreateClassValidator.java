package validator;

import dto.CreateAccountDto;

public class CreateClassValidator {

	private CreateClassValidator() {
	}

	private static class SingletonHolder {
		private static CreateClassValidator INSTANCE = new CreateClassValidator();
	}

	public CreateClassValidator getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public ValidationErrors isValid(CreateAccountDto account) {

	}
}
