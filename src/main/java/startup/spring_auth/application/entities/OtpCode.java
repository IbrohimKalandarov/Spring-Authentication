package startup.spring_auth.application.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import startup.spring_auth.application.entities.temp.BaseEntity;

import java.time.Instant;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OtpCode extends BaseEntity {

    @Column(nullable = false)
    private Instant expiryTime;

    @Column(nullable = false)
    private Integer code;
}
