package shop.woowasap.payment.domain.out;

import shop.woowasap.payment.domain.Payment;

public interface PaymentRepository {

    Payment save(Payment payment);
}
