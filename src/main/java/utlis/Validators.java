package utlis;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;

import java.util.Locale;

public class Validators {

    public static ValidatorFactory defaultFactory;

    static {
        setUpValidator();
    }

    private static void setUpValidator() {
        Locale.setDefault(Locale.US);
        defaultFactory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ResourceBundleMessageInterpolator(
                        new PlatformResourceBundleLocator("CustomValidationMessages")
                ))
                .buildValidatorFactory();
    }

    public static void closeDefaultFactory() {
        defaultFactory.close();
    }
}
