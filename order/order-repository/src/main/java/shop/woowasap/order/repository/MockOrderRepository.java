package shop.woowasap.order.repository;

import org.springframework.stereotype.Repository;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.out.OrderRepository;

@Repository
public class MockOrderRepository implements OrderRepository {

    @Override
    public void persist(final Order order) {
    }
}
