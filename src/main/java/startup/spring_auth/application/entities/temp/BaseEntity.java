package startup.spring_auth.application.entities.temp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

/**
 * <h3>Base Entity Class</h3>
 *
 */

@Setter
@Getter
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, name = "created_at")
    private Instant createdAt;

    @CreatedBy
    @Column(updatable = false, name = "create_by")
    private Long createdBy;

    @CreationTimestamp
    @Column(nullable = false, name = "update_at")
    private Instant updatedAt;

    @LastModifiedBy
    @Column(insertable = false, name = "update_by")
    private Long updatedBy;
}
