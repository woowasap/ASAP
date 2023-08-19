package shop.woowasap.payment.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.Instant;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "created_at", columnDefinition = "TIMESTAMP(6)", nullable = false, updatable = false)
    protected Instant createdAt;
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP(6)", nullable = false)
    protected Instant updatedAt;

    @PrePersist
    void prePersist() {
        final Instant now = Instant.now();

        createdAt = createdAt != null ? createdAt : now;
        updatedAt = updatedAt != null ? updatedAt : now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = updatedAt != null ? updatedAt : Instant.now();
    }
}
