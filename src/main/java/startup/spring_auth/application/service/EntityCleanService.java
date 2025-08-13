package startup.spring_auth.application.service;

import org.springframework.stereotype.Component;

@Component
public interface EntityCleanService {

    void deleteExpiredTokens();

    void deleteExpiredOtpCode();

    void deleteExpiredForgotPasswordReset();
}
