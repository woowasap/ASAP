package shop.woowasap.payment.domain.in;

import shop.woowasap.payment.domain.in.request.PaymentRequest;
import shop.woowasap.payment.domain.in.response.PaymentResponse;

public interface PaymentUseCase {

    PaymentResponse pay(PaymentRequest paymentRequest);
}
