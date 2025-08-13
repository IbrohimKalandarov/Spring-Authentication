package startup.spring_auth.application.payload.request;

import startup.spring_auth.application.validator.ValidName;

import java.io.Serializable;

public record ForgotPassResetDTO(@ValidName String newPassword, String tempToken) implements Serializable {
}
