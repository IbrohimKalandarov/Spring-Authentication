package startup.spring_auth.application.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import startup.spring_auth.application.entities.*;
import startup.spring_auth.application.entities.enums.TokenType;
import startup.spring_auth.application.exception.BadRequestException;
import startup.spring_auth.application.exception.ConflictException;
import startup.spring_auth.application.exception.NotFoundException;
import startup.spring_auth.application.mapper.AddressMapper;
import startup.spring_auth.application.mapper.TokenMapper;
import startup.spring_auth.application.mapper.UserMapper;
import startup.spring_auth.application.payload.ApiResponse;
import startup.spring_auth.application.payload.request.ChangePassDTO;
import startup.spring_auth.application.payload.request.ForgotPassResetDTO;
import startup.spring_auth.application.payload.request.LoginDTO;
import startup.spring_auth.application.payload.request.RegisterDTO;
import startup.spring_auth.application.payload.response.AuthResponse;
import startup.spring_auth.application.repository.ForgotPasswordResetRepository;
import startup.spring_auth.application.repository.TokenRepository;
import startup.spring_auth.application.repository.UserRepository;
import startup.spring_auth.application.security.jwt.JwtAuthenticationFilter;
import startup.spring_auth.application.security.jwt.JwtTokenProvider;
import startup.spring_auth.application.service.AuthService;
import startup.spring_auth.application.service.OtpCodeService;
import startup.spring_auth.application.service.SmsService;
import startup.spring_auth.application.utils.Util;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final ForgotPasswordResetRepository forgotPasswordResetRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final OtpCodeService otpCodeService;
    private final AddressMapper addressMapper;
    private final TokenMapper tokenMapper;
    private final SmsService smsService;
    private final UserMapper userMapper;
    private final Util util;

    @Override
    public ApiResponse<String> register(RegisterDTO dto) {
        userRepository.findByPhoneNumber(dto.phoneNumber()).ifPresent(user -> {
            throw new ConflictException("Phone Number Already Exists");
        });

        Address address = addressMapper.toAddress(dto);

        OtpCode otpCode = otpCodeService.saveOtpCode();

        User user = userMapper.toUser(dto);

        user.setAddress(address);
        user.setOtpCode(otpCode);

        userRepository.save(user);

        logger.info("USER REGISTERED SUCCESSFULLY User id: {}", user.getId());

        return ApiResponse.success(null, "Registered Successfully");
    }

    @Override
    public ApiResponse<String> verifyPhoneWithOtpCode(Integer otpCode) {

//     Agar otp code null yoki 6 ta raqamdan iborat bo'lmasa xatolikka otadi
        if (!util.isSixDigit(otpCode)) {
            throw new BadRequestException("Code is Null or Wrong");
        }

//      Otp Code bo'yicha userni qidiradi topolmasa xatolikka otadi
        User user = userRepository.findByOtpCode_Code(otpCode).orElseThrow(
                () -> new BadRequestException("Wrong Otp Code"));

//        Agar otp code vaqti tugagan bo'lsa xatolikka otadi va uni o'chirib tashlaydi chunki endi uni keragi yo'q
        if (!otpCodeService.isValid(user.getOtpCode())) {
            user.setOtpCode(null); // userni otp codini null qilinganda otp code bazadan avtomatik o'chib ketadi
            userRepository.save(user);
            throw new BadRequestException("Code is Invalid");
        }

        user.setOtpCode(null);
        user.setEnabled(true);

        userRepository.save(user);

        logger.info("USER VERIFIED SUCCESSFULLY User id: {}", user.getId());

        return ApiResponse.success(null, "Phone Verifying Successfully");
    }

    @Override
    public ApiResponse<String> resendOtpCode(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new NotFoundException("User Not Found"));

//        agar userni enabled=true va otp code==null bo'lsa unda registratsiyadan to'g'ri o'tgan va
//        balkim adashib bosdi qayta otp yuborishni yoki logikaga bog'liq o'zgaradi bu yog'i
        if (user.isEnabled() && user.getOtpCode() == null) {
            throw new BadRequestException("This is Wrong");
        }

//        agar user enabled=false bo'lsa unda tekshiriladi avval otp kod bo'lsa va amal qilsa boshqa jo'natilmasin
        if (!user.isEnabled() && user.getOtpCode() != null && otpCodeService.isValid(user.getOtpCode())) {
            throw new BadRequestException("You Have a Valid Code");
        }

        user.setOtpCode(otpCodeService.saveOtpCode());

        userRepository.save(user);

        return ApiResponse.success(null, "New Code Sending Successfully");
    }

    @Override
    public ApiResponse<AuthResponse> login(LoginDTO dto) {
        User user = userRepository.findByPhoneNumber(dto.phoneNumber()).orElseThrow(
                () -> new NotFoundException("User Not Found"));

//        Agar password noto'g'ri bo'lsa xatolikka otadi
        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new BadRequestException("Wrong Password");
        }

//        Agar otp code bo'lsa va hali muddati o'tmagan va user enabled=false bo'lsa user hali tasdiqdan o'tmagan bo'ladi
        if (user.getOtpCode() != null && otpCodeService.isValid(user.getOtpCode()) && !user.isEnabled()) {
            throw new BadRequestException("Get Registered Code");
        }

//        Agar otp bo'lsa va otpni vaqti o'tgan bo'lsa unda yangi kod jo'natish kerak va kodni bazadan o'chirish kerak
        if (user.getOtpCode() != null && !otpCodeService.isValid(user.getOtpCode())) {
            user.setOtpCode(null);
            userRepository.save(user);
            throw new BadRequestException("Get Registered New Code");
        }

//        Agar otp code null bo'lsa va enabled=false bo'lsa unda u otp code tasdiqlagan lekin faol emas bo'ladi
//        bunda qo'shimcha logika bo'yicha harakat qilamiz
        if (user.getOtpCode() == null && !user.isEnabled()) {
            throw new BadRequestException("User Not Active");
        }

        AuthResponse authResponse = userMapper.toTokenDTO(user);

        logger.info("LOGIN SUCCESSFULLY User phoneNumber: {}", user.getPhoneNumber());

        return ApiResponse.success(authResponse, "Token Sending Successfully");
    }


    @Override
    public ApiResponse<String> forgotPassword(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new NotFoundException("User Not Found"));

        if (!user.isEnabled()) {
            throw new BadRequestException("Phone number is not verified yet");
        }

        String token = UUID.randomUUID().toString();

        ForgotPasswordReset resetToken = ForgotPasswordReset.builder()
                .tempToken(token)
                .user(user)
                .expiryDate(Instant.now().plusSeconds(1800))
                .build();

        forgotPasswordResetRepository.save(resetToken);

        String resetUrl = "http://localhost:8080/api/auth/check-forgot-password?token=" + token;

        String response = "Reset link sent to your phone number. Temp Token: [" + token + "]";

        // Send URL yoki Code via SMS/Email
        smsService.sendMessage(user.getPhoneNumber(), "Reset your password: " + resetUrl + "\n token: " + token);

        logger.info("RESET PASSWORD SUCCESSFULLY SEND FOR USER. User phoneNumber: {}", user.getPhoneNumber());

        return ApiResponse.success(null, response);
    }

    @Override
    public ApiResponse<String> checkForgotPassword(ForgotPassResetDTO dto) {
        ForgotPasswordReset expiredToken = forgotPasswordResetRepository.findByTempToken(dto.tempToken()).orElseThrow(
                () -> new NotFoundException("Invalid or Expired token/code"));

        if (expiredToken.getExpiryDate().isBefore(Instant.now())) {
            throw new BadRequestException("Token has Expired");
        }

        User user = expiredToken.getUser();

        user.setPassword(passwordEncoder.encode(dto.newPassword()));

        userRepository.save(user);

        forgotPasswordResetRepository.delete(expiredToken);

        logger.info("RESET PASSWORD CHECKING SUCCESSFULLY FOR USER. User id: {}", user.getId());

        return ApiResponse.success(null, "Password Updated Successfully");
    }


    @Override
    public ApiResponse<String> refreshToken(HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getUserFromRequest(request);

//        token saqlandimi??????????????????
        String accessToken = jwtTokenProvider.generateAccessToken(user.getPhoneNumber(), user.getRole().toString());

        return ApiResponse.success(accessToken, "Token Sending Successfully");
    }


    @Override
    public ApiResponse<String> logout(HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getUserFromRequest(request);

        List<Token> tokenList = tokenRepository.findByPhoneNumberAndTypeIn(user.getPhoneNumber(), List.of(TokenType.ACCESS, TokenType.REFRESH));

//      Userga tegishli bo'lgan barcha tokenlarni black list typega o'tqazadi
        for (Token tk : tokenList) {
            if (tk.getType().equals(TokenType.REFRESH)) {
                tokenMapper.saveBlackToken(tk.getToken(), tk.getPhoneNumber());
                logger.info("LOGOUT SUCCESSFULLY [REFRESH TOKEN] PHONE NUMBER: {}", tk.getPhoneNumber());
            } else if (tk.getType().equals(TokenType.ACCESS)) {
                tokenMapper.saveBlackToken(tk.getToken(), tk.getPhoneNumber());
                logger.info("LOGOUT SUCCESSFULLY [ACCESS TOKEN] PHONE NUMBER: {}", tk.getPhoneNumber());
            }
        }

        return ApiResponse.success(null, "Log out Successfully");
    }

    @Override
    public ApiResponse<String> changePassword(ChangePassDTO dto,HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getUserFromRequest(request);

        if (!passwordEncoder.matches(dto.oldPassword(), user.getPassword())) {
            throw new BadRequestException("Wrong Old Password");
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));

        userRepository.save(user);

        return ApiResponse.success(null, "Password Updated Successfully");
    }

}
