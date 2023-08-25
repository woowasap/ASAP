package shop.woowasap.shop.repository.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.woowasap.shop.repository.entity.CartEntity;

public interface CartJpaRepository extends JpaRepository<CartEntity, Long> {

    Optional<CartEntity> findByUserId(final Long userId);

    boolean existsByUserId(final Long userId);
}
