package shop.woowasap.order.domain.out;

import java.util.Optional;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.out.response.OrdersPaginationResponse;

public interface OrderRepository {

    void create(final Order order);

    void persist(final Order order);

    OrdersPaginationResponse findAllOrderByUserId(final long userId, final int page, final int size);

    Optional<Order> findOrderByOrderIdAndUserId(final long orderId, final long userId);
}
