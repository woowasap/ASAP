package shop.woowasap.payment.service.support;

import java.math.BigInteger;
import shop.woowasap.payment.domain.PayStatus;
import shop.woowasap.payment.domain.PayType;
import shop.woowasap.payment.domain.Payment;

public final class PaymentFixture {

    private static final long DEFAULT_ORDER_ID = 1L;
    private static final long DEFAULT_USER_ID = 1L;

    private PaymentFixture() {
        throw new UnsupportedOperationException();
    }

    public static Payment defaultPayment() {
        return Payment.builder()
            .paymentId(1234L)
            .orderId(DEFAULT_ORDER_ID)
            .userId(DEFAULT_USER_ID)
            .purchasedMoney(BigInteger.valueOf(10000))
            .payType(PayType.CARD)
            .payStatus(PayStatus.PENDING)
            .build();
    }

}
