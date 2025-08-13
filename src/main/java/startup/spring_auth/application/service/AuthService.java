package startup.spring_auth.application.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import startup.spring_auth.application.payload.ApiResponse;
import startup.spring_auth.application.payload.request.*;
import startup.spring_auth.application.payload.response.AuthResponse;

@Component
public interface AuthService {

    ApiResponse<String> register(RegisterDTO dto);

    ApiResponse<AuthResponse> login(LoginDTO dto);

    ApiResponse<String> forgotPassword(String phoneNumber);

    ApiResponse<String> logout(HttpServletRequest request);

    ApiResponse<String> changePassword(ChangePassDTO dto,HttpServletRequest request);

    ApiResponse<String> verifyPhoneWithOtpCode(Integer otpCode);

    ApiResponse<String> resendOtpCode(String phoneNumber);

    ApiResponse<String> checkForgotPassword(ForgotPassResetDTO dto);

    ApiResponse<String> refreshToken(HttpServletRequest request);

}
