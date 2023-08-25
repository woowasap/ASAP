package shop.woowasap.order.repository.jpa;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.woowasap.order.repository.entity.OrderEntity;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByIdAndUserId(final long orderId, final long userId);

    Page<OrderEntity> findAllByUserId(final long userId, Pageable pageable);
}
