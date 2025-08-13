package startup.spring_auth.application.mapper;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import startup.spring_auth.application.entities.Address;
import startup.spring_auth.application.entities.enums.Neighborhood;
import startup.spring_auth.application.entities.enums.Street;
import startup.spring_auth.application.exception.NotFoundException;
import startup.spring_auth.application.payload.request.RegisterDTO;
import startup.spring_auth.application.payload.response.ResAddressDTO;
import startup.spring_auth.application.repository.AddressRepository;

@Component
@RequiredArgsConstructor
public class AddressMapper {
    private final AddressRepository addressRepository;

    public Address toAddress(@NotNull RegisterDTO dto) {
        Address address = Address.builder()
                .neighborhood(getNeighborhood(dto.address().neighborhoodName()))
                .street(getStreet(dto.address().streetName()))
                .homeNumber(dto.address().homeNumber())
                .build();
        addressRepository.save(address);
        return address;
    }

    public ResAddressDTO toAddressDTO(@NotNull Address address) {
        return ResAddressDTO.builder()
                .street(address.getStreet())
                .neighborhood(address.getNeighborhood())
                .homeNumber(address.getHomeNumber())
                .build();
    }

    private Street getStreet(String streetName) {
        for (Street street : Street.values()) {
            if (streetName.equalsIgnoreCase(street.name())) {
                return street;
            }
        }
        throw new NotFoundException("Street Not Found");
    }

    private Neighborhood getNeighborhood(String neighborhoodName) {
        for (Neighborhood neighborhood : Neighborhood.values()) {
            if (neighborhoodName.equalsIgnoreCase(neighborhood.name())) {
                return neighborhood;
            }
        }
        throw new NotFoundException("Neighborhood Not Found");
    }
}
