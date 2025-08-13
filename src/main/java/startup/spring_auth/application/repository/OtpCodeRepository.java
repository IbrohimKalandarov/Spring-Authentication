package startup.spring_auth.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import startup.spring_auth.application.entities.OtpCode;

import java.time.Instant;

public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {
    void deleteOtpCodeByExpiryTimeAfter(Instant expiryTimeAfter);
}
