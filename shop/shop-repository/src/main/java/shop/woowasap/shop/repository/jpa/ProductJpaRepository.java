package shop.woowasap.shop.repository.jpa;

import java.time.Instant;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.woowasap.shop.repository.entity.ProductEntity;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByIdAndEndTimeAfter(final long productId,
        final Instant nowTime);

    Page<ProductEntity> findAllByEndTimeAfter(final Instant nowTime, final Pageable pageable);

    Slice<ProductEntity> findAllWithV3ByEndTimeAfter(
        final Instant nowTime,
        final Pageable pageable
    );

    @Query("SELECT p FROM ProductEntity p WHERE p.endTime > :nowTime AND (p.startTime > :startTime OR (p.startTime = :startTime AND p.id > :productId))")
//    @Query(value = "select * from product where end_time > :nowTime and (start_time, product_id) > (:startTime, :productId) order by start_time, product_id asc limit 20", nativeQuery = true)
    Slice<ProductEntity> findAllByEndTimeAfterWithV2(
        @Param("nowTime") final Instant nowTime,
        @Param("startTime") final Instant startTime,
        @Param("productId") final Long productId,
        final Pageable pageable
    );

    Page<ProductEntity> findAll(final Pageable pageable);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update ProductEntity as p set p.quantity = p.quantity - :consumedQuantity where p.id = :productId")
    void consumeQuantity(@Param("productId") final long productId,
        @Param("consumedQuantity") final long consumedQuantity);
}
