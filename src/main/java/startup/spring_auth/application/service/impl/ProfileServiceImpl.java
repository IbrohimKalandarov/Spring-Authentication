package startup.spring_auth.application.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import startup.spring_auth.application.entities.User;
import startup.spring_auth.application.exception.ConflictException;
import startup.spring_auth.application.mapper.UserMapper;
import startup.spring_auth.application.payload.ApiResponse;
import startup.spring_auth.application.payload.request.ProfilePatchDTO;
import startup.spring_auth.application.payload.response.UserDTO;
import startup.spring_auth.application.repository.UserRepository;
import startup.spring_auth.application.security.jwt.JwtAuthenticationFilter;
import startup.spring_auth.application.service.ProfileService;

@Service
@Slf4j
@RequiredArgsConstructor
@Primary
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    Logger logger = LoggerFactory.getLogger(ProfileServiceImpl.class);

    @Override
    public ApiResponse<String> profileMePatch(ProfilePatchDTO dto, HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getUserFromRequest(request);

        if (!dto.firstName().isBlank() && !user.getFirstName().equals(dto.firstName())) {
            user.setFirstName(dto.firstName());
        }

        if (!dto.lastName().isBlank() && !user.getLastName().equals(dto.lastName())) {
            user.setLastName(dto.lastName());
        }

        if (!dto.fatherName().isBlank() && !user.getFatherName().equals(dto.fatherName())) {
            user.setFatherName(dto.fatherName());
        }

        userRepository.save(user);

        logger.info("PROFILE UPDATE SUCCESSFULLY User id: {}", user.getId());

        return ApiResponse.success(null, "Profile Update Successfully");
    }

    @Override
    public ApiResponse<UserDTO> profileMeGet(@NonNull HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getUserFromRequest(request);

        UserDTO userDTO = userMapper.toUserDTO(user);

        return ApiResponse.success(userDTO, "User Find Successfully");
    }


    @Override
    public ApiResponse<String> updatePhoneNumber(String phoneNumber, HttpServletRequest request) {
        User user = jwtAuthenticationFilter.getUserFromRequest(request);

//         agar nomer bo'lsa va u o'ziniki bo'lmasa  conflict bo'lmasa set number -> qo'shimcha xavfsizlik qo'shiladi
        if (!user.getPhoneNumber().equals(phoneNumber)) {

//          kirib kelgan phone Number bo'yicha databasedan user qidiriladi agar mavjud bo'lsa unda conflict bo'ladi va update qilinmaydi
            if (userRepository.findByPhoneNumber(phoneNumber).isPresent()) {
                throw new ConflictException("User Already Exists");
            }

            user.setPhoneNumber(phoneNumber); // phone number o'zgartirish
            userRepository.save(user);

            logger.info("PHONE NUMBER UPDATE SUCCESSFULLY User id: {}", user.getId());

            return ApiResponse.success(null, "Profile update successfully");
        }

        return ApiResponse.success(null, "You have this phone number");
    }
}
