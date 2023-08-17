package shop.woowasap.order.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import java.time.Instant;

@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "created_at", columnDefinition = "TIMESTAMP(6)", nullable = false, updatable = false)
    protected Instant createdAt;

    @PrePersist
    void prePersist() {
        Instant now = Instant.now();

        createdAt = createdAt != null ? createdAt : now;
    }
}
