package shop.woowasap.payment.domain.in;

import shop.woowasap.order.domain.out.event.StockFailEvent;
import shop.woowasap.order.domain.out.event.StockSuccessEvent;

public interface PaymentEventConsumer {

    void successPayment(final StockSuccessEvent stockSuccessEvent);

    void cancelPayment(final StockFailEvent stockFailEvent);
}
