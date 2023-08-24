package shop.woowasap.payment.domain.out;

import java.util.Optional;
import shop.woowasap.payment.domain.Payment;

public interface PaymentRepository {

    Payment create(final Payment payment);

    Payment persist(final Payment payment);

    Optional<Payment> findByOrderId(final long orderId);
}
