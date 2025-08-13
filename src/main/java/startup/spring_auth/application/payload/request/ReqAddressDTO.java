package startup.spring_auth.application.payload.request;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record ReqAddressDTO(@NotBlank String neighborhoodName, @NotBlank String streetName,
                            @NotBlank String homeNumber) implements Serializable {
}