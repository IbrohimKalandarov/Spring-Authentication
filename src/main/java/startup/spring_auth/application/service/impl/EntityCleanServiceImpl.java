package startup.spring_auth.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import startup.spring_auth.application.repository.ForgotPasswordResetRepository;
import startup.spring_auth.application.repository.OtpCodeRepository;
import startup.spring_auth.application.repository.TokenRepository;
import startup.spring_auth.application.security.jwt.JwtTokenProvider;
import startup.spring_auth.application.service.EntityCleanService;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Primary
@RequiredArgsConstructor
public class EntityCleanServiceImpl implements EntityCleanService {

    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final OtpCodeRepository otpCodeRepository;
    private final ForgotPasswordResetRepository forgotPasswordResetRepository;

    // Har kuni soat 02:00 da ishlaydi
    @Scheduled(cron = "0 0 2 * * *")
    public void deleteExpiredTokens() {
        System.out.println("Expired bo'lgan Tokenlarni o'chiruvchi method ishlashni boshladi!");
        AtomicInteger count = new AtomicInteger();
        tokenRepository.findAll().forEach(token -> {
            if (jwtTokenProvider.isExpiredToken(token.getToken())) {
                count.getAndIncrement();
                tokenRepository.delete(token);
            }
        });
        System.out.println("\nO'chirilgan tokenlar soni: " + count + " ta\n");
    }

    // Har kuni soat 02:00 da ishlaydi
    @Scheduled(cron = "0 0 2 * * *")
    public void deleteExpiredOtpCode() {
        System.out.println("Expired bo'lgan Otp Codelarni o'chiruvchi method ishlashni boshladi!");
        otpCodeRepository.deleteOtpCodeByExpiryTimeAfter(Instant.now());
    }

    // Har kuni soat 02:00 da ishlaydi
    @Scheduled(cron = "0 0 2 * * *")
    public void deleteExpiredForgotPasswordReset() {
        System.out.println("Expired bo'lgan ForgotPasswordResetlarni o'chiruvchi method ishlashni boshladi!");
        forgotPasswordResetRepository.deleteForgotPasswordResetByExpiryDateIsAfter(Instant.now());
    }
}
