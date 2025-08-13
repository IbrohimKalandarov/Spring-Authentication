package startup.spring_auth.application.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    // Barcha Uzbekiston mobil operatorlari uchun regex
    private static final Pattern UZ_PHONE_PATTERN = Pattern.compile(
            "^998(3[3-9]\\d|4[0-9]\\d|5[0-9]\\d|6[0-9]\\d|7[0-9]\\d|8[8-9]\\d|9[0-9]\\d)\\d{6}$"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Objects.isNull(value) || value.trim().isEmpty()) {
            return false;
        }

        // Faqat raqamlardan iboratligini tekshiramiz
        if (!value.matches("^\\d+$")) {
            return false;
        }

        // 12 ta raqamdan iboratligi (998XXXXXXXXX)
        if (value.length() != 12) {
            return false;
        }

        return UZ_PHONE_PATTERN.matcher(value).matches();
    }
}
