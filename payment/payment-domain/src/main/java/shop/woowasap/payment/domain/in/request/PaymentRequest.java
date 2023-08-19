package shop.woowasap.payment.domain.in.request;

import shop.woowasap.payment.domain.PayType;

public record PaymentRequest(
    Long orderId,
    Long userId,
    PayType payType,
    Boolean isSuccess

) {

}
