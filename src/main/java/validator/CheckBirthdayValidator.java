package validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class CheckBirthdayValidator implements ConstraintValidator<CheckBirthday, LocalDate> {

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return Period.between(value, LocalDate.now()).getYears() > 18;
    }
}
