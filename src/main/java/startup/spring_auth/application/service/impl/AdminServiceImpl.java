package startup.spring_auth.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import startup.spring_auth.application.entities.User;
import startup.spring_auth.application.entities.enums.Role;
import startup.spring_auth.application.exception.BadRequestException;
import startup.spring_auth.application.exception.NotFoundException;
import startup.spring_auth.application.mapper.UserMapper;
import startup.spring_auth.application.payload.ApiResponse;
import startup.spring_auth.application.payload.request.ChangeRoleDTO;
import startup.spring_auth.application.payload.response.UserDTO;
import startup.spring_auth.application.repository.UserRepository;
import startup.spring_auth.application.service.AdminService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@Primary
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public ApiResponse<List<UserDTO>> findAllUsers() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            return ApiResponse.success(Collections.emptyList(), "Empty List");
        }

        List<UserDTO> userDTOs = userMapper.toUserDTO(users);

        return ApiResponse.success(userDTOs, "Find All Users");
    }

    @Override
    public ApiResponse<List<Role>> findUserRoles() {
        List<Role> roles = new ArrayList<>(Arrays.asList(Role.values()));

        return ApiResponse.success(roles, "Find All Roles");
    }

    @Override
    public ApiResponse<UserDTO> findUserById(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Id is Invalid");
        }

        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User Not Found"));

        UserDTO userDTO = userMapper.toUserDTO(user);

        return ApiResponse.success(userDTO, "User Found Successfully");
    }

    @Override
    public ApiResponse<String> changeUserRole(ChangeRoleDTO dto) {
        User user = userRepository.findById(dto.userId()).orElseThrow(
                () -> new NotFoundException("User Not Found"));

        for (Role value : Role.values()) {
            if (value.name().equalsIgnoreCase(dto.roleName())) {

                user.setRole(Role.valueOf(dto.roleName().toUpperCase()));

                userRepository.save(user);

                logger.info("CHANGE ROLE SUCCESSFULLY User id: {}", user.getId());

                return ApiResponse.success(null, "Role Changed Successfully");
            }
        }

        return ApiResponse.error("Role Not Found", 404);
    }

}
