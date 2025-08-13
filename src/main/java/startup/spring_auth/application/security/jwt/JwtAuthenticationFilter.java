package startup.spring_auth.application.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.filter.OncePerRequestFilter;
import startup.spring_auth.application.entities.User;
import startup.spring_auth.application.entities.enums.TokenType;
import startup.spring_auth.application.exception.NotFoundException;
import startup.spring_auth.application.repository.TokenRepository;
import startup.spring_auth.application.repository.UserRepository;

import java.io.IOException;
import java.util.List;

import static startup.spring_auth.application.security.jwt.JwtTokenProvider.SECRET_KEY;


@Component
@RequiredArgsConstructor
@CrossOrigin
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${spring.security.whitelist}")
    private String[] whiteList;
    private final JwtTokenProvider jwtProvider;
    private final UserRepository userRepository;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (isWhitelisted(request.getServletPath())) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = extractJwtFromHeader(request);

        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (tokenRepository.findByToken(jwt).isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is Invalid");
            return;
        }

        tokenRepository.findByToken(jwt).ifPresent(token -> {
            if (token.getType().equals(TokenType.BLACK)) {
                try {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is blacklisted");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        try {
            String phoneNumber = jwtProvider.extractPhoneNumber(jwt);
            if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                authenticateUser(request, jwt, phoneNumber);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            sendErrorResponse(response, "Invalid JWT token: " + e.getMessage());
        }

    }

    public User getUserFromRequest(HttpServletRequest request) {
        String token = extractJwtFromHeader(request);

        if (token == null) {
            throw new IllegalArgumentException("Invalid token");
        }

        jwtProvider.isValidToken(token);

        String phoneNumber = jwtProvider.extractPhoneNumber(token);

        return userRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new NotFoundException("User Not Found"));
    }

    /**
     * So‘rov yo‘li whitelistda ekanligini tekshiradi.
     *
     * @param requestPath So‘rov yo‘li
     * @return Whitelistda bo‘lsa true, aks holda false
     */
    private boolean isWhitelisted(String requestPath) {
        for (String path : whiteList) {
            if (pathMatcher.match(path, requestPath)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Authorization header dan JWT tokenni ajratib oladi.
     *
     * @param request HTTP so‘rov
     * @return JWT token yoki null
     */
    public String extractJwtFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring("Bearer ".length());
        }
        return null;
    }


    /**
     * Foydalanuvchini autentifikatsiya qiladi va SecurityContext’ga o‘rnatadi.
     *
     * @param request  HTTP so‘rov
     * @param jwt      JWT token
     * @param username Foydalanuvchi nomi
     */
    private void authenticateUser(HttpServletRequest request, String jwt, String username) {
        jwtProvider.isValidToken(jwt);

        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(jwt)
                .getBody();

        String role = claims.get("role", String.class);
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(username, null, authorities);

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    /**
     * Xato bo‘lganda mijozga javob yuboradi.
     *
     * @param response HTTP javob
     * @param message  Xato xabari
     */
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }

}
