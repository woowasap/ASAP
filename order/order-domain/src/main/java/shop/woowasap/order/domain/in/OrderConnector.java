package shop.woowasap.order.domain.in;

import java.util.Optional;
import shop.woowasap.order.domain.Order;

public interface OrderConnector {

    Optional<Order> findByOrderId(final long orderId);

}
