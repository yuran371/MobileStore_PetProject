package validator;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import dto.CreateAccountDto;
import entity.Country;
import entity.Gender;
import utlis.DateFormatter;

public class CreateAccountValidator {

	ValidationErrors validationErrors = ValidationErrors.getInstance();

	private CreateAccountValidator() {
	}

	private static class SingletonHolder {
		private static CreateAccountValidator INSTANCE = new CreateAccountValidator();
	}

	public static CreateAccountValidator getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public ValidationErrors isValid(CreateAccountDto account) {
		List<Error> createAccountErrors = validationErrors.getCreateAccountErrors();
		if (!createAccountErrors.isEmpty()) {
			createAccountErrors.removeAll(createAccountErrors);
		}
		if (!DateFormatter.isValid(account.getBirthday())) {
			createAccountErrors.add(new Error("Date is invalid",
					"Date of birth is invalid. Please, check your birthday date is correct. "));
		}
		if (!Country.isValid(account.getCountry())) {
			createAccountErrors.add(new Error("Country is invalid",
					"Country is invalid. Please, check country is choosen and correct."));
		}
		if (!Gender.isValid(account.getGender())) {
			createAccountErrors.add(new Error("Gender is invalid",
					"Choosen gender is invalid. Please, check that choosen gender is correct"));
		}
		
		if (Period.between(LocalDate.now(), DateFormatter.getDate(account.getBirthday())).getYears() > 18) {		// > 18 пропускает; < 18 не пропускает
			createAccountErrors.add(new Error("Age is invalid", "Age is invalid. To register you must be over 18 y.o."));
		}
		return validationErrors;
	}
}