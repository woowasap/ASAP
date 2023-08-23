package shop.woowasap.order.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.OrderType;
import shop.woowasap.order.domain.exception.DoesNotFindOrderException;
import shop.woowasap.order.domain.exception.DoesNotOrderedException;
import shop.woowasap.order.domain.in.OrderConnector;
import shop.woowasap.order.domain.out.OrderRepository;
import shop.woowasap.shop.domain.in.product.ProductConnector;

@Service
@RequiredArgsConstructor
public class OrderConnectorService implements OrderConnector {

    private final OrderRepository orderRepository;
    private final ProductConnector productConnector;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public Optional<Order> findByOrderIdAndUserId(final long orderId, final long userId) {
        return orderRepository.findOrderByOrderIdAndUserId(orderId, userId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void consumeStock(final long orderId, final long userId) {
        final Order order = getOrder(orderId, userId);
        try {
            consumeProducts(order);
        } catch (Exception exception) {
            persistOrderType(order, OrderType.FAIL);
            throw new DoesNotOrderedException();
        }
        persistOrderType(order, OrderType.SUCCESS);
    }

    private Order getOrder(final long orderId, final long userId) {
        return orderRepository.findOrderByOrderIdAndUserId(orderId, userId)
            .orElseThrow(() -> new DoesNotFindOrderException(orderId, userId));
    }

    private void consumeProducts(final Order order) {
        order.getOrderProducts().forEach(
            orderProduct -> productConnector.consumeProductByProductId(
                orderProduct.getProductId(),
                orderProduct.getQuantity())
        );
    }

    private void persistOrderType(final Order order, final OrderType orderType) {
        orderRepository.persist(order.updateOrderType(orderType));
    }
}
