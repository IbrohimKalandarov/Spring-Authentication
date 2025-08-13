package startup.spring_auth.application.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import startup.spring_auth.application.entities.User;
import startup.spring_auth.application.entities.enums.Role;
import startup.spring_auth.application.payload.request.RegisterDTO;
import startup.spring_auth.application.payload.response.AuthResponse;
import startup.spring_auth.application.payload.response.UserDTO;
import startup.spring_auth.application.security.jwt.JwtTokenProvider;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;
    private final AddressMapper addressMapper;
    private final JwtTokenProvider jwtTokenProvider;

    public User toUser(RegisterDTO dto) {
        return User.builder().
                firstName(dto.firstName())
                .lastName(dto.lastName())
                .fatherName(dto.fatherName())
                .phoneNumber(dto.phoneNumber())
                .password(passwordEncoder.encode(dto.password()))
                .role(Role.USER)
                .enabled(false)
                .build();
    }

    public UserDTO toUserDTO(User user) {
        UserDTO build = UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fatherName(user.getFatherName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .addressDTO(addressMapper.toAddressDTO(user.getAddress()))
                .build();
        if (user.getOtpCode() != null) {
            build.setOtpCode(user.getOtpCode().getCode());
        }
        return build;
    }

    public List<UserDTO> toUserDTO(List<User> users) {
        List<UserDTO> dto_list = new ArrayList<>();
        users.forEach(u -> {
            dto_list.add(toUserDTO(u));
        });
        return dto_list;
    }

    public AuthResponse toTokenDTO(User user) {
        return AuthResponse.builder()
                .accessToken(jwtTokenProvider.generateAccessToken(user.getPhoneNumber(), user.getRole().toString()))
                .refreshToken(jwtTokenProvider.generateRefreshToken(user.getPhoneNumber(), user.getRole().toString()))
                .build();
    }

}
