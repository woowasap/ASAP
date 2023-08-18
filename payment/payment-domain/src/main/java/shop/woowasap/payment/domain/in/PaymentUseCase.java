package shop.woowasap.payment.domain.in;

public interface PaymentUseCase {

    void pay(final long orderId, final String payType, final boolean isSuccess);
}
