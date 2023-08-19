package shop.woowasap.payment.domain.out;

import java.util.Optional;
import shop.woowasap.payment.domain.Payment;

public interface PaymentRepository {

    Payment save(final Payment payment);

    Optional<Payment> findByOrderId(final long orderId);
}
