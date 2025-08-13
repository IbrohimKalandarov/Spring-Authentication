package startup.spring_auth.application.service;

import org.springframework.stereotype.Component;

@Component
public interface SmsService {

    void sendOtpCode(Integer otpCode);

    void sendMessage(String phoneNumber, String sms);
}
