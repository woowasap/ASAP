package shop.woowasap.shop.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.woowasap.shop.repository.entity.CartEntity;

public interface CartJpaRepository extends JpaRepository<CartEntity, Long> {
}
