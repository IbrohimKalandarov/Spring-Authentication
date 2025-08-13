package startup.spring_auth.application.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import startup.spring_auth.application.entities.Token;
import startup.spring_auth.application.entities.enums.TokenType;
import startup.spring_auth.application.repository.TokenRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TokenMapper {
    private final TokenRepository tokenRepository;


    public void saveRefreshToken(String token, String phoneNumber) {
        Token build = Token.builder()
                .token(token)
                .phoneNumber(phoneNumber)
                .type(TokenType.REFRESH)
                .build();

        tokenRepository.save(build);
    }

    public void saveAccessToken(String token, String phoneNumber) {
        Token build = Token.builder()
                .token(token)
                .phoneNumber(phoneNumber)
                .type(TokenType.ACCESS)
                .build();

        tokenRepository.save(build);
    }

    public void saveBlackToken(String token, String phoneNumber) {

        Optional<Token> byToken = tokenRepository.findByToken(token);
        Token build;
        if (byToken.isPresent()) {
            build = byToken.get();
            build.setType(TokenType.BLACK);
        } else {
            build = Token.builder()
                    .token(token)
                    .phoneNumber(phoneNumber)
                    .type(TokenType.BLACK)
                    .build();
        }

        tokenRepository.save(build);
    }

}
