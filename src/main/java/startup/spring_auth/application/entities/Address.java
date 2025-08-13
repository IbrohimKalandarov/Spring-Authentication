package startup.spring_auth.application.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import startup.spring_auth.application.entities.enums.Neighborhood;
import startup.spring_auth.application.entities.enums.Street;
import startup.spring_auth.application.entities.temp.BaseEntity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Address extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Neighborhood neighborhood;

    @Enumerated(EnumType.STRING)
    private Street street;

    private String homeNumber;
}
