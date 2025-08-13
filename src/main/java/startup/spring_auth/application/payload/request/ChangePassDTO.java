package startup.spring_auth.application.payload.request;

import startup.spring_auth.application.validator.ValidPassword;

import java.io.Serializable;

public record ChangePassDTO(@ValidPassword String oldPassword,
                            @ValidPassword String newPassword) implements Serializable {
}
