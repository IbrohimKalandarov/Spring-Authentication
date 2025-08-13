package startup.spring_auth.application.service;

import org.springframework.stereotype.Component;
import startup.spring_auth.application.entities.OtpCode;

@Component
public interface OtpCodeService {

    OtpCode saveOtpCode();

    boolean isValid(OtpCode otpCode);
}
