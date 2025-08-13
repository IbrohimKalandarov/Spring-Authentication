package startup.spring_auth.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import startup.spring_auth.application.service.SmsService;

@Service
@Primary
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {
    @Override
    public void sendOtpCode(Integer otpCode) {
//        otp code jo'natiladi;
        System.out.println("\nSENDING OTP CODE: " + otpCode + "\n");
    }

    @Override
    public void sendMessage(String phoneNumber, String sms) {
//         sms jo'natiladi
        System.out.println("\nSENDING MESSAGE FOR FORGOT PASSWORD. SMS: " + sms + "\nPHONE NUMBER: " + phoneNumber + "\n");
    }
}
