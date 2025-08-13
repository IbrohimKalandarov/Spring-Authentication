package startup.spring_auth.application.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NameValidator.class)
@Documented
public @interface ValidName {
    String message() default "Wrong! length must be between 3 and 40";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
