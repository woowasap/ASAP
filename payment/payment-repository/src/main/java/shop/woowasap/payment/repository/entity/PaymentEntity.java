package shop.woowasap.payment.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.math.BigInteger;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.woowasap.payment.domain.PayStatus;
import shop.woowasap.payment.domain.PayType;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "payment", indexes = {@Index(name = "index_order_id", columnList = "order_id")})
public class PaymentEntity extends BaseEntity {

    @Id
    @Column(name = "payment_id", nullable = false)
    private Long paymentId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "purchased_money", nullable = false)
    private BigInteger purchasedMoney;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "pay_type", length = 16, nullable = false)
    private PaymentEntityType payType;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "pay_status", length = 16, nullable = false)
    private PaymentEntityStatus payStatus;

    @Builder
    public PaymentEntity(Long paymentId, Long userId, Long orderId, BigInteger purchasedMoney,
        PayType payType, PayStatus payStatus) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.orderId = orderId;
        this.purchasedMoney = purchasedMoney;
        this.payType = PaymentEntityType.valueOf(payType.getValue());
        this.payStatus = PaymentEntityStatus.valueOf(payStatus.getValue());
    }
}
