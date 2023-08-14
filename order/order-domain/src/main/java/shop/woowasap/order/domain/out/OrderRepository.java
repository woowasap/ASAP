package shop.woowasap.order.domain.out;

import shop.woowasap.order.domain.Order;

public interface OrderRepository {

    void persist(Order order);
}
