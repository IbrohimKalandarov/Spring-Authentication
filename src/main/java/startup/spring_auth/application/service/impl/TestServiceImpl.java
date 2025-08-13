package startup.spring_auth.application.service.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import startup.spring_auth.application.payload.ApiResponse;
import startup.spring_auth.application.service.TestService;

@Service
@Primary
public class TestServiceImpl implements TestService {

    @Override
    public ApiResponse<String> blocked() {
        return ApiResponse.success("BLOCKED URL", "TEST SUCCESSFULLY");
    }

    @Override
    public ApiResponse<String> nonBlocked() {
        return ApiResponse.success("NON BLOCKING URL", "TEST SUCCESSFULLY");
    }
}
