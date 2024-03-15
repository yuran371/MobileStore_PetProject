package factory;

import jakarta.enterprise.inject.Produces;
import jakarta.validation.Validator;
import utlis.Validators;

public class ValidatorFactory {

    @Produces
    public Validator getValidator() {
        return Validators.defaultFactory.getValidator();
    }
}
