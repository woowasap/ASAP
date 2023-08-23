package shop.woowasap.order.domain.in;

import java.util.Optional;
import shop.woowasap.order.domain.Order;

public interface OrderConnector {

    Optional<Order> findByOrderIdAndUserId(final long orderId, final long userId);

    void consumeStock(final long orderId, final long userId);

}
