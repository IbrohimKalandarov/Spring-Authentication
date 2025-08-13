package startup.spring_auth.application.service;

import org.springframework.stereotype.Component;
import startup.spring_auth.application.payload.ApiResponse;

@Component
public interface TestService {

    ApiResponse<String> blocked();

    ApiResponse<String> nonBlocked();
}
