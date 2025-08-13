package startup.spring_auth.application.payload.response;

import lombok.Builder;
import startup.spring_auth.application.entities.enums.Neighborhood;
import startup.spring_auth.application.entities.enums.Street;

import java.io.Serializable;

@Builder
public record ResAddressDTO(String homeNumber, Neighborhood neighborhood, Street street) implements Serializable {
}
