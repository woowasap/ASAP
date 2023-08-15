package shop.woowasap.order.repository;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Repository;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.out.OrderRepository;
import shop.woowasap.order.domain.out.response.OrdersPaginationResponse;

@Repository
public class MockOrderRepository implements OrderRepository {

    @Override
    public void persist(final Order order) {
    }

    @Override
    public OrdersPaginationResponse findAllOrderByUserId(final long userId, final int page, final int size) {
        return null;
    }
}
