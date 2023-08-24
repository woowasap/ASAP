package shop.woowasap.auth.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class UserEntity extends BaseEntity implements Persistable<Long> {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "username", length = 25, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "user_type", length = 16, nullable = false)
    private UserEntityType userType;

    @Transient
    private boolean isNew = true;

    @Builder
    public UserEntity(final Long id, final String username, final String password,
        final UserEntityType userType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostLoad
    @PostPersist
    protected void loadOrPersist() {
        this.isNew = false;
    }
}
