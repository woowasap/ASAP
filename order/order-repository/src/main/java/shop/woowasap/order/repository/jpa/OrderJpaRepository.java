package shop.woowasap.order.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.woowasap.order.repository.entity.OrderEntity;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
}
