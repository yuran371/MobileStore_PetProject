package validator;

import dao.PersonalAccountDao;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private PersonalAccountDao personalAccountDao;

    private static Weld weld = new Weld();
    private static WeldContainer weldContainer = weld.initialize();

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        personalAccountDao = weldContainer.select(PersonalAccountDao.class).get();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return personalAccountDao.getByEmail(value).isEmpty();
    }
}
