package startup.spring_auth.application.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface ValidPassword {
    String message() default "Password length must be between 3 and 40";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
