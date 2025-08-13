package startup.spring_auth.application.logger;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * Request Body dan kelgan habarlarga log yozadigan class consolga
 */
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(request);

        String requestBody = new BufferedReader(new InputStreamReader(cachedRequest.getInputStream()))
                .lines().collect(Collectors.joining("\n"));

        System.out.println("Request body: " + requestBody);

        filterChain.doFilter(cachedRequest, response);
    }
}
