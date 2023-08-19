package shop.woowasap.payment.repository;

import shop.woowasap.payment.domain.PayStatus;
import shop.woowasap.payment.domain.PayType;
import shop.woowasap.payment.domain.Payment;
import shop.woowasap.payment.repository.entity.PaymentEntity;

public class PaymentEntityMapper {

    private PaymentEntityMapper() {
    }

    public static PaymentEntity toEntity(final Payment payment) {
        return PaymentEntity.builder()
            .paymentId(payment.getPaymentId())
            .orderId(payment.getOrderId())
            .userId(payment.getUserId())
            .purchasedMoney(payment.getPurchasedMoney())
            .payStatus(payment.getPayStatus())
            .payType(payment.getPayType())
            .build();
    }

    public static Payment toDomain(final PaymentEntity entity) {
        return Payment.builder()
            .paymentId(entity.getPaymentId())
            .orderId(entity.getOrderId())
            .userId(entity.getUserId())
            .purchasedMoney(entity.getPurchasedMoney())
            .payStatus(PayStatus.valueOf(entity.getPayStatus().getValue()))
            .payType(PayType.valueOf(entity.getPayType().getValue()))
            .createdAt(entity.getCreatedAt())
            .build();
    }
}
