package shop.woowasap.order.repository;

import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.out.OrderRepository;

public class MockOrderRepository implements OrderRepository {

    @Override
    public void persist(final Order order) {
    }
}
