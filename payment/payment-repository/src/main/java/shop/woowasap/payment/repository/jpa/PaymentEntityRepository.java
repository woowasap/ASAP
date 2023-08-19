package shop.woowasap.payment.repository.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.woowasap.payment.repository.entity.PaymentEntity;

public interface PaymentEntityRepository extends JpaRepository<PaymentEntity, Long> {

    Optional<PaymentEntity> findByOrderId(final long orderId);
}
