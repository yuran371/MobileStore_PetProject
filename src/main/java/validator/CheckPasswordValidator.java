package validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class CheckPasswordValidator implements ConstraintValidator<CheckPassword, String> {

    public final Pattern textPattern = Pattern.compile("^(?=.*\\p{Lu})(?=.*\\p{Ll})(?=.*\\d).+$");
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        boolean matches = textPattern.matcher(value).matches();
        return matches;
    }
}
