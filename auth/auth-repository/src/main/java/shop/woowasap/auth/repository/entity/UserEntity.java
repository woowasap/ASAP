package shop.woowasap.auth.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class UserEntity extends BaseEntity {

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

    @Builder
    public UserEntity(final Long id, final String username, final String password,
        final UserEntityType userType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }
}
