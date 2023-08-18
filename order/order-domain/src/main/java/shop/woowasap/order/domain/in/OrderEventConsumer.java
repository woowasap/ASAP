package shop.woowasap.order.domain.in;

import shop.woowasap.order.domain.in.event.PayFailEvent;
import shop.woowasap.order.domain.in.event.PaySuccessEvent;

public interface OrderEventConsumer {

    void listenPayFailEvent(final PayFailEvent payFailEvent);

    void listenPaySuccessEvent(final PaySuccessEvent paySuccessEvent);
}
