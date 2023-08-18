package shop.woowasap.order.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.OrderProduct;
import shop.woowasap.order.domain.exception.DoesNotFindOrderException;
import shop.woowasap.order.domain.exception.DoesNotOrderedException;
import shop.woowasap.order.domain.in.OrderEventConsumer;
import shop.woowasap.order.domain.in.event.PayFailEvent;
import shop.woowasap.order.domain.in.event.PaySuccessEvent;
import shop.woowasap.order.domain.out.OrderRepository;
import shop.woowasap.order.domain.out.event.StockFailEvent;
import shop.woowasap.order.domain.out.event.StockSuccessEvent;
import shop.woowasap.order.service.support.fixture.OrderFixture;
import shop.woowasap.order.service.support.fixture.OrderProductFixture;
import shop.woowasap.order.service.support.fixture.ProductFixture;
import shop.woowasap.shop.domain.in.product.ProductConnector;
import shop.woowasap.shop.domain.product.Product;

@RecordApplicationEvents
@ExtendWith(SpringExtension.class)
@DisplayName("OrderEventServiceTest 클래스")
@ContextConfiguration(classes = OrderEventService.class)
class OrderEventServiceTest {

    @Autowired
    private ApplicationEvents applicationEvents;

    @Autowired
    private OrderEventConsumer orderEventConsumer;

    @MockBean
    private ProductConnector productConnector;

    @MockBean
    private OrderRepository orderRepository;

    @Nested
    @DisplayName("listenPayFailEvent 메소드는")
    class listenPayFailEventMethod {

        @Test
        @DisplayName("PayFailEvent를 받아, Order의 상태를 Fail로 변경한다")
        void updateOrderTypeFail() {
            // given
            final long orderId = 1L;
            final long userId = 2L;
            final PayFailEvent payFailEvent = new PayFailEvent(orderId, userId);

            final Product product = ProductFixture.getDefaultBuilder().build();
            final OrderProduct orderProduct = OrderProductFixture.from(product);
            final Order defaultOrder = OrderFixture.getDefault(List.of(orderProduct));

            when(orderRepository.findOrderByOrderIdAndUserId(orderId, userId)).thenReturn(
                Optional.of(defaultOrder));

            // when
            Exception result = catchException(
                () -> orderEventConsumer.listenPayFailEvent(payFailEvent));

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("OrderId에 해당하는 Order를 찾을 수 없는경우, DoesNotFindOrderException을 던진다.")
        void throwDoesNotFindOrderExceptionWhenOrderNotExists() {
            // given
            final long orderId = 1L;
            final long userId = 2L;
            final PayFailEvent payFailEvent = new PayFailEvent(orderId, userId);

            when(orderRepository.findOrderByOrderIdAndUserId(orderId, userId)).thenReturn(
                Optional.empty());

            // when
            Exception result = catchException(
                () -> orderEventConsumer.listenPayFailEvent(payFailEvent));

            // then
            assertThat(result).isInstanceOf(DoesNotFindOrderException.class);
        }
    }

    @Nested
    @DisplayName("listenPaySuccessEvent 메소드는")
    class listenPaySuccessEventMethod {

        @Test
        @DisplayName("PaySuccessEvent를 받아, Order의 상태를 Success로 변경한다")
        void updateOrderTypeSuccess() {
            // given
            final long orderId = 1L;
            final long userId = 2L;
            final PaySuccessEvent paySuccessEvent = new PaySuccessEvent(orderId, userId);

            final Product product = ProductFixture.getDefaultBuilder().build();
            final OrderProduct orderProduct = OrderProductFixture.from(product);
            final Order defaultOrder = OrderFixture.getDefault(List.of(orderProduct));

            when(orderRepository.findOrderByOrderIdAndUserId(orderId, userId)).thenReturn(
                Optional.of(defaultOrder));

            // when
            Exception result = catchException(
                () -> orderEventConsumer.listenPaySuccessEvent(paySuccessEvent));

            // then
            int count = (int) applicationEvents.stream(StockSuccessEvent.class).count();
            assertThat(result).isNull();
            assertThat(count).isOne();
        }

        @Test
        @DisplayName("PaySuccessEvent를 받아, Order의 상태를 Fail로 변경한다")
        void updateOrderTypeFail() {
            // given
            final long orderId = 1L;
            final long userId = 2L;
            final PaySuccessEvent paySuccessEvent = new PaySuccessEvent(orderId, userId);

            final Product product = ProductFixture.getDefaultBuilder().build();
            final OrderProduct orderProduct = OrderProductFixture.from(product);
            final Order defaultOrder = OrderFixture.getDefault(List.of(orderProduct));

            when(orderRepository.findOrderByOrderIdAndUserId(orderId, userId)).thenReturn(
                Optional.of(defaultOrder));
            doThrow(RuntimeException.class).when(productConnector)
                .consumeProductByProductId(product.getId(), product.getQuantity().getValue());

            // when
            Exception result = catchException(
                () -> orderEventConsumer.listenPaySuccessEvent(paySuccessEvent));

            // then
            int count = (int) applicationEvents.stream(StockFailEvent.class).count();
            assertThat(result).isInstanceOf(DoesNotOrderedException.class);
            assertThat(count).isOne();
        }

        @Test
        @DisplayName("OrderId에 해당하는 Order를 찾을 수 없는경우, DoesNotFindOrderException을 던진다.")
        void throwDoesNotFindOrderExceptionWhenOrderNotExists() {
            // given
            final long orderId = 1L;
            final long userId = 2L;
            final PaySuccessEvent paySuccessEvent = new PaySuccessEvent(orderId, userId);

            when(orderRepository.findOrderByOrderIdAndUserId(orderId, userId)).thenReturn(
                Optional.empty());

            // when
            Exception result = catchException(
                () -> orderEventConsumer.listenPaySuccessEvent(paySuccessEvent));

            // then
            assertThat(result).isInstanceOf(DoesNotFindOrderException.class);
        }
    }
}
