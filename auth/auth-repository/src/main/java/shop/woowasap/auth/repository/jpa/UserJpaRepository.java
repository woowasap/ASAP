package shop.woowasap.auth.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.woowasap.auth.repository.entity.UserEntity;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
}
