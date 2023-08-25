package shop.woowasap.payment.domain.out;

import java.util.List;
import java.util.Optional;
import shop.woowasap.payment.domain.Payment;

public interface PaymentRepository {

    Payment save(final Payment payment);

    Optional<Payment> findByOrderId(final long orderId);

    List<Payment> findAllByOrderId(final long orderId);
}
