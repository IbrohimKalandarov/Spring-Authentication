package startup.spring_auth.application.payload.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import startup.spring_auth.application.entities.enums.Role;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class UserDTO implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    private String fatherName;

    private String phoneNumber;

    private Role role;

    private boolean enabled;

    private Integer otpCode;

    private ResAddressDTO addressDTO;

}
