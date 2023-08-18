package shop.woowasap.payment.domain.out;

import java.util.Optional;
import shop.woowasap.payment.domain.Payment;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findByOrderId(long orderId);
}
