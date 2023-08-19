package shop.woowasap.payment.domain.out;

import java.util.List;
import shop.woowasap.payment.domain.Payment;

public interface PaymentRepository {

    Payment save(final Payment payment);

    List<Payment> findAllByOrderId(final long orderId);
}
