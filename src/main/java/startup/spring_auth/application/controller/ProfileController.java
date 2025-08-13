package startup.spring_auth.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import startup.spring_auth.application.payload.ApiResponse;
import startup.spring_auth.application.payload.request.ProfilePatchDTO;
import startup.spring_auth.application.payload.response.UserDTO;
import startup.spring_auth.application.service.ProfileService;
import startup.spring_auth.application.validator.ValidPhoneNumber;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService service;

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<String>> profileMePatch(@RequestBody ProfilePatchDTO dto, @NonNull HttpServletRequest request) {
        return ResponseEntity.ok().body(service.profileMePatch(dto, request));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDTO>> profileMeGet(@NonNull HttpServletRequest request) {
        return ResponseEntity.ok().body(service.profileMeGet(request));
    }

    @PostMapping("/update-phoneNumber/{phoneNumber}")
    @Operation(summary = "Phone Number ni almashtirish uchun")
    public ResponseEntity<ApiResponse<String>> profileUpdatePhoneNumber(@PathVariable @ValidPhoneNumber String phoneNumber, @NonNull HttpServletRequest request) {
        return ResponseEntity.ok().body(service.updatePhoneNumber(phoneNumber, request));
    }
}
