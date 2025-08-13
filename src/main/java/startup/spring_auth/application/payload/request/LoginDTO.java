package startup.spring_auth.application.payload.request;

import startup.spring_auth.application.validator.ValidPassword;
import startup.spring_auth.application.validator.ValidPhoneNumber;

import java.io.Serializable;

public record LoginDTO(@ValidPhoneNumber String phoneNumber, @ValidPassword String password) implements Serializable {
}
