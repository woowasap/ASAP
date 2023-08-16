package shop.woowasap.shop.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.woowasap.shop.repository.entity.ProductEntity;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
}
