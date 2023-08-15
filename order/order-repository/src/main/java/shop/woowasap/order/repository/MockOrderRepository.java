package shop.woowasap.order.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.out.OrderRepository;
import shop.woowasap.order.domain.out.response.OrdersPaginationResponse;

@Repository
public class MockOrderRepository implements OrderRepository {

    private final Map<Long, Order> userIdPerOrderRepository = new HashMap<>();

    @Override
    public void persist(final Order order) {
        userIdPerOrderRepository.put(order.getUserId(), order);
    }

    @Override
    public OrdersPaginationResponse findAllOrderByUserId(final long userId, final int page,
        final int size) {

        return new OrdersPaginationResponse(List.of(userIdPerOrderRepository.get(userId)), page,
            size);
    }

    @Override
    public Optional<Order> findOrderByOrderIdAndUserId(final long orderId, final long userId) {
        final Order order = userIdPerOrderRepository.get(userId);
        if (order == null || order.getId() != orderId) {
            return Optional.empty();
        }
        return Optional.of(order);
    }
}
