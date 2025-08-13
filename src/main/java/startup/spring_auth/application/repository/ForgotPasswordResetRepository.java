package startup.spring_auth.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import startup.spring_auth.application.entities.ForgotPasswordReset;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface ForgotPasswordResetRepository extends JpaRepository<ForgotPasswordReset, Long> {
    Optional<ForgotPasswordReset> findByTempToken(String tempToken);

    void deleteForgotPasswordResetByExpiryDateIsAfter(Instant expiryDateAfter);
}
