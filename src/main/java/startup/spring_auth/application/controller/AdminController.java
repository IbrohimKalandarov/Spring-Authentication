package startup.spring_auth.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import startup.spring_auth.application.entities.enums.Role;
import startup.spring_auth.application.payload.ApiResponse;
import startup.spring_auth.application.payload.request.ChangeRoleDTO;
import startup.spring_auth.application.payload.response.UserDTO;
import startup.spring_auth.application.service.AdminService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAnyRole('SUPER_ADMIN' , 'ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService service;

    @GetMapping("/user/{id}")
    @Operation(summary = "userni id bo'yicha qidirish")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok().body(service.findUserById(id));
    }

    @GetMapping("/users")
    @Operation(summary = "barcha userlarni qidirish")
    public ResponseEntity<ApiResponse<List<UserDTO>>> findAllUsers() {
        return ResponseEntity.ok().body(service.findAllUsers());
    }

    @GetMapping("/roles")
    @Operation(summary = "barcha user role larni qidirish")
    public ResponseEntity<ApiResponse<List<Role>>> getUserRoles() {
        return ResponseEntity.ok().body(service.findUserRoles());
    }

    @PostMapping("/change-role")
    @Operation(description = "userni role ini almashtirish")
    public ResponseEntity<ApiResponse<String>> changeUserRole(@RequestBody @Valid ChangeRoleDTO dto) {
        return ResponseEntity.ok().body(service.changeUserRole(dto));
    }

}
