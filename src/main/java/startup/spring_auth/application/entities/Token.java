package startup.spring_auth.application.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;
import startup.spring_auth.application.entities.enums.TokenType;
import startup.spring_auth.application.entities.temp.BaseEntity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Token extends BaseEntity {

    @Column(nullable = false, unique = true)
    String token;

    @Column(nullable = false)
    String phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    TokenType type;
}
