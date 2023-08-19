package shop.woowasap.payment.repository.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.woowasap.payment.repository.entity.PaymentEntity;

public interface PaymentEntityRepository extends JpaRepository<PaymentEntity, Long> {

    List<PaymentEntity> findByOrderIdOrdOrderByUpdatedAtDesc(final long orderId);
}
