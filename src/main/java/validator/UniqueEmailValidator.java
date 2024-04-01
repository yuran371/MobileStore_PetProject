package validator;

import dao.PersonalAccountDao;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private static final PersonalAccountDao personalAccountDao = CDI.current().select(PersonalAccountDao.class).get();


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return personalAccountDao.getByEmail(value).isEmpty();
    }
}
