package shop.woowasap.payment.repository.jpa;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.woowasap.payment.repository.entity.PaymentEntity;

public interface PaymentEntityRepository extends JpaRepository<PaymentEntity, Long> {

    List<PaymentEntity> findByOrderIdOrderByUpdatedAtDesc(final long orderId);

    Optional<PaymentEntity> findByOrderId(long orderId);
}
