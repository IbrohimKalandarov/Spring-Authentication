package startup.spring_auth.application.service;

import org.springframework.stereotype.Component;
import startup.spring_auth.application.entities.enums.Role;
import startup.spring_auth.application.payload.ApiResponse;
import startup.spring_auth.application.payload.request.ChangeRoleDTO;
import startup.spring_auth.application.payload.response.UserDTO;

import java.util.List;

@Component
public interface AdminService {

    ApiResponse<List<UserDTO>> findAllUsers();

    ApiResponse<List<Role>> findUserRoles();

    ApiResponse<UserDTO> findUserById(Long id);

    ApiResponse<String> changeUserRole(ChangeRoleDTO dto);

}
