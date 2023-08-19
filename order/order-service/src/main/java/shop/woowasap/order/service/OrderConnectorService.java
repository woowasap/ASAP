package shop.woowasap.order.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.in.OrderConnector;
import shop.woowasap.order.domain.out.OrderRepository;

@Service
@RequiredArgsConstructor
public class OrderConnectorService implements OrderConnector {

    private final OrderRepository orderRepository;

    @Override
    public Optional<Order> findByOrderIdAndUserId(final long orderId, final long userId) {
        return orderRepository.findOrderByOrderIdAndUserId(orderId, userId);
    }
}
