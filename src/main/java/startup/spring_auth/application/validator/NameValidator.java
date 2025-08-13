package startup.spring_auth.application.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<ValidName, String> {
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 40;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }

        int length = value.length();
        return length >= MIN_LENGTH && length <= MAX_LENGTH;
    }
}
