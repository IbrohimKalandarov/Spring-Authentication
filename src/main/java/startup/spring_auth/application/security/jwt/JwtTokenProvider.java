package startup.spring_auth.application.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import startup.spring_auth.application.mapper.TokenMapper;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private static final String KEY = "[I]&h[B]$v[R]c*[O]%6[H]n^[I]f&[M]d8<-SPRING-AUTH->*j&2^E%dF1D^F64Aflm13489tyt8u25lnk@q25wv#efr91/lwe(f)mn[9ubl.k1n34i[uhb9185y9";
    protected static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(KEY.getBytes());
    private static final long access_ttl = 1000L * 60 * 10; // 10 daqiqa
    private static final long refresh_ttl = 1000L * 60 * 60 * 24 * 20; // 20 kun
    private final TokenMapper tokenMapper;
    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);


    public String generateAccessToken(String phoneNumber, String role) {
        long now = System.currentTimeMillis();
        String access_token = Jwts.builder()
                .setSubject(phoneNumber)
                .claim("role", role)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + access_ttl))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        tokenMapper.saveAccessToken(access_token, phoneNumber);

        return access_token;
    }


    public String generateRefreshToken(String phoneNumber, String role) {
        long now = System.currentTimeMillis();
        Instant ex_time = Instant.ofEpochMilli(now + refresh_ttl);

        String refresh_token = Jwts.builder()
                .setSubject(phoneNumber)
                .claim("role", role)
                .setIssuedAt(new Date(now))
                .setExpiration(Date.from(ex_time))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        tokenMapper.saveRefreshToken(refresh_token, phoneNumber);

        return refresh_token;
    }

    public String extractPhoneNumber(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();

        } catch (ExpiredJwtException e) {
            throw new JwtException("Jwt token has expired");
        } catch (SignatureException e) {
            throw new JwtException("Invalid Jwt signature");
        } catch (Exception e) {
            throw new JwtException("Invalid Jwt token");
        }
    }

    public void isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new JwtException("Jwt token has expired");
        } catch (SignatureException e) {
            throw new JwtException("Invalid Jwt signature");
        } catch (Exception e) {
            throw new JwtException("Invalid Jwt token");
        }
    }

    public boolean isExpiredToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            return true;
        }
        return false;
    }
}
