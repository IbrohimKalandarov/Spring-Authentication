package startup.spring_auth.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import startup.spring_auth.application.entities.Token;
import startup.spring_auth.application.entities.enums.TokenType;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    List<Token> findByPhoneNumberAndTypeIn(String phoneNumber, Collection<TokenType> types);

}
