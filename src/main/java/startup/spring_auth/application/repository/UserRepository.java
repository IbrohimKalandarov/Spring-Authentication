package startup.spring_auth.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import startup.spring_auth.application.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber")
    Optional<User> findByPhoneNumberFetchOtp(@Param("phoneNumber") String phoneNumber);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByOtpCode_Code(Integer otpCodeCode);
}
