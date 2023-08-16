package shop.woowasap.shop.repository.jpa;

import java.time.Instant;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.woowasap.shop.repository.entity.ProductEntity;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByIdAndEndTimeAfter(final long productId,
        final Instant nowTime);

    Page<ProductEntity> findAllByEndTimeAfter(final Instant nowTime, final Pageable pageable);

    Page<ProductEntity> findAll(final Pageable pageable);
}
