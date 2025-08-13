package startup.spring_auth.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import startup.spring_auth.application.entities.OtpCode;
import startup.spring_auth.application.repository.OtpCodeRepository;
import startup.spring_auth.application.service.OtpCodeService;
import startup.spring_auth.application.service.SmsService;
import startup.spring_auth.application.utils.Util;

import java.time.Instant;

@Component
@Primary
@RequiredArgsConstructor
public class OtpCodeServiceImpl implements OtpCodeService {
    private final OtpCodeRepository otpCodeRepository;
    private final SmsService smsService;
    private final Util util;

    @Override
    public OtpCode saveOtpCode() {

        //  2 daqiqa amal qiladi
        Instant time = Instant.now().plusSeconds(120);

        OtpCode otpCode = otpCodeRepository.save(OtpCode.builder()
                .code(util.generateRandomNumber()).expiryTime(time).build());

        smsService.sendOtpCode(otpCode.getCode());

        return otpCode;
    }


    //   code amal qilish muddatini tekshiradi. Hozirgi vaqtgacha hali vaqti bo'lsa true aks holda  false
    @Override
    public boolean isValid(OtpCode otpCode) {
        return Instant.now().isBefore(otpCode.getExpiryTime());
    }
}
