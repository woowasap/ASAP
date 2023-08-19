package shop.woowasap.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.OrderType;
import shop.woowasap.order.domain.exception.DoesNotFindOrderException;
import shop.woowasap.order.domain.exception.DoesNotOrderedException;
import shop.woowasap.order.domain.in.OrderEventConsumer;
import shop.woowasap.order.domain.in.event.PayFailEvent;
import shop.woowasap.order.domain.in.event.PaySuccessEvent;
import shop.woowasap.order.domain.out.OrderRepository;
import shop.woowasap.order.domain.out.event.StockFailEvent;
import shop.woowasap.order.domain.out.event.StockSuccessEvent;
import shop.woowasap.shop.domain.in.product.ProductConnector;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderEventService implements OrderEventConsumer {

    private final OrderRepository orderRepository;
    private final ProductConnector productConnector;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Async
    @Override
    @EventListener(PayFailEvent.class)
    public void listenPayFailEvent(final PayFailEvent payFailEvent) {
        final Order order = getOrder(payFailEvent.orderId(), payFailEvent.userId());

        persistOrderType(order, OrderType.FAIL);
    }

    @Async
    @Override
    @EventListener(PaySuccessEvent.class)
    public void listenPaySuccessEvent(final PaySuccessEvent paySuccessEvent) {
        final Order order = getOrder(paySuccessEvent.orderId(), paySuccessEvent.userId());

        try {
            consumeProducts(order);
        } catch (Exception exception) {
            persistOrderType(order, OrderType.FAIL);
            applicationEventPublisher.publishEvent(new StockFailEvent(paySuccessEvent.orderId()));
            throw new DoesNotOrderedException();
        }

        persistOrderType(order, OrderType.SUCCESS);
        applicationEventPublisher.publishEvent(new StockSuccessEvent(paySuccessEvent.orderId()));
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
        order.updateOrderType(orderType);
        orderRepository.persist(order);
    }
}
