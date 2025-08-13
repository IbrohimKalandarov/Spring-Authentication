package startup.spring_auth.application.payload.request;

import java.io.Serializable;

public record ProfilePatchDTO(String firstName, String lastName, String fatherName) implements Serializable {
}
