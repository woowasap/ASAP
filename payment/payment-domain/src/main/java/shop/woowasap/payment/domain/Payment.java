package shop.woowasap.payment.domain;

import java.math.BigInteger;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Payment {

    private final long paymentId;
    private final long userId;
    private final long orderId;
    private final BigInteger purchasedMoney;
    private final Instant createdAt;
    private final PayType payType;
    private final PayStatus payStatus;

    @Builder
    public Payment(final long paymentId, final long userId, final long orderId,
        final BigInteger purchasedMoney, final Instant createdAt, final PayType payType,
        final PayStatus payStatus) {

        this.paymentId = paymentId;
        this.userId = userId;
        this.orderId = orderId;
        this.purchasedMoney = purchasedMoney;
        this.createdAt = createdAt;
        this.payType = payType;
        this.payStatus = payStatus;
    }

    public Payment changeStatus(final PayStatus payStatus) {
        return Payment.builder()
            .paymentId(paymentId)
            .userId(userId)
            .orderId(orderId)
            .purchasedMoney(purchasedMoney)
            .createdAt(createdAt)
            .payType(payType)
            .payStatus(payStatus)
            .build();
    }
}
