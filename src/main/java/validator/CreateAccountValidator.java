package validator;

import dto.personalAccount.CreateAccountDto;
import entity.enums.CountryEnum;
import entity.enums.GenderEnum;
import utlis.DateFormatter;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class CreateAccountValidator {

	ValidationErrors validationErrors = ValidationErrors.getInstance();

	private CreateAccountValidator() {
	}

	private static class SingletonHolder {
		private static final CreateAccountValidator INSTANCE = new CreateAccountValidator();
	}

	public static CreateAccountValidator getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public ValidationErrors isValid(CreateAccountDto account) {
		List<Error> createAccountErrors = validationErrors.getCreateAccountErrors();
		if (!createAccountErrors.isEmpty()) {
			createAccountErrors.clear();
		}
		if (!DateFormatter.isValid(account.getBirthday())) {
			createAccountErrors.add(new Error("Date is invalid",
					"Date of birth is invalid. Please, check your birthday date is correct. "));
		}
		if (!CountryEnum.isValid(account.getCountry())) {
			createAccountErrors.add(new Error("Country is invalid",
					"Country is invalid. Please, check country is choosen and correct."));
		}
		if (!GenderEnum.isValid(account.getGender())) {
			createAccountErrors.add(new Error("Gender is invalid",
					"Choosen gender is invalid. Please, check that choosen gender is correct"));
		}
		if (Period.between(DateFormatter.getDate(account.getBirthday()), LocalDate.now()).getYears() < 18) {
			createAccountErrors
					.add(new Error("Age is invalid", "Age is invalid. To register you must be over 18 y.o."));
		}
		return validationErrors;
	}
}
