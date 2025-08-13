package startup.spring_auth.application.payload.request;

import jakarta.validation.Valid;
import startup.spring_auth.application.validator.ValidName;
import startup.spring_auth.application.validator.ValidPassword;
import startup.spring_auth.application.validator.ValidPhoneNumber;

import java.io.Serializable;

public record RegisterDTO(
        @ValidPhoneNumber String phoneNumber, @ValidName String firstName,
        @ValidName String lastName, @ValidName String fatherName, @ValidPassword String password,
        @Valid ReqAddressDTO address) implements Serializable {
}
