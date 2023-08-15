package shop.woowasap.order.domain.out;

import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.out.response.OrdersPaginationResponse;

public interface OrderRepository {

    void persist(final Order order);

    OrdersPaginationResponse findAllOrderByUserId(final long userId, final int page,
        final int size);
}
