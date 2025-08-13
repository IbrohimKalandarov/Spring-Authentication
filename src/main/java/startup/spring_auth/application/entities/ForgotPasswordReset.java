package startup.spring_auth.application.entities;

import jakarta.persistence.*;
import lombok.*;
import startup.spring_auth.application.entities.temp.BaseEntity;

import java.time.Instant;

@Entity
@Table(name = "forgot_password_reset")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordReset extends BaseEntity {

    @Column(name = "temp_token")
    private String tempToken;

    @Column(name = "expiry_date")
    private Instant expiryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
