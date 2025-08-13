package startup.spring_auth.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import startup.spring_auth.application.payload.ApiResponse;
import startup.spring_auth.application.payload.request.ChangePassDTO;
import startup.spring_auth.application.payload.request.ForgotPassResetDTO;
import startup.spring_auth.application.payload.request.LoginDTO;
import startup.spring_auth.application.payload.request.RegisterDTO;
import startup.spring_auth.application.payload.response.AuthResponse;
import startup.spring_auth.application.service.AuthService;
import startup.spring_auth.application.validator.ValidPhoneNumber;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody @Valid RegisterDTO dto) {
        return ResponseEntity.ok().body(service.register(dto));
    }

    @PostMapping("/verify-phone/{code}")
    @Operation(description = "Otp code ni tasdiqlash uchun")
    public ResponseEntity<ApiResponse<String>> verifyPhoneWithOtpCode(@PathVariable @NonNull Integer code) {
        return ResponseEntity.ok(service.verifyPhoneWithOtpCode(code));
    }

    @PostMapping("/reset-otp-code/{phoneNumber}")
    @Operation(description = "Otp code qayta yuborish uchun")
    public ResponseEntity<ApiResponse<String>> resendOtpCode(@PathVariable @ValidPhoneNumber String phoneNumber) {
        return ResponseEntity.ok(service.resendOtpCode(phoneNumber));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Valid LoginDTO dto) {
        return ResponseEntity.ok().body(service.login(dto));
    }

    @PostMapping("refresh-token")
    public ResponseEntity<ApiResponse<String>> refreshToken(@NonNull HttpServletRequest request) {
        return ResponseEntity.ok().body(service.refreshToken(request));
    }

    @PostMapping("/forgot-password/{phoneNumber}")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@PathVariable @ValidPhoneNumber String phoneNumber) {
        return ResponseEntity.ok().body(service.forgotPassword(phoneNumber));
    }

    @PostMapping("/check-forgot-password")
    @Operation(description = "forgot password uchun yuborilgan sms codni tasdiqlash va yangi parol o'rnatish uchun")
    public ResponseEntity<ApiResponse<String>> checkForgotPassword(@RequestBody @Valid ForgotPassResetDTO dto) {
        return ResponseEntity.ok().body(service.checkForgotPassword(dto));
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@RequestBody @Valid ChangePassDTO dto, @NonNull HttpServletRequest request) {
        return ResponseEntity.ok().body(service.changePassword(dto, request));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@NonNull HttpServletRequest request) {
        return ResponseEntity.ok().body(service.logout(request));
    }
}
