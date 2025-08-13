package startup.spring_auth.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import startup.spring_auth.application.payload.ApiResponse;
import startup.spring_auth.application.service.TestService;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {
    private final TestService service;

    @GetMapping("/blocked")
    public ResponseEntity<ApiResponse<String>> blocked() {
        return ResponseEntity.ok(service.blocked());
    }

    @GetMapping("/non-blocked")
    public ResponseEntity<ApiResponse<String>> nonBlocked() {
        return ResponseEntity.ok(service.nonBlocked());
    }

}
