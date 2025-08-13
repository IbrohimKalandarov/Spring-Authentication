package startup.spring_auth.application.payload.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record ChangeRoleDTO(@NotNull Long userId, @NotEmpty String roleName) implements Serializable {
}
