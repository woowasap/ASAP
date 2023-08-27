package shop.woowasap.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.OrderProduct;
import shop.woowasap.order.domain.exception.DoesNotFindOrderException;
import shop.woowasap.order.domain.exception.DoesNotOrderedException;
import shop.woowasap.order.domain.out.OrderRepository;
import shop.woowasap.order.service.support.fixture.OrderFixture;
import shop.woowasap.order.service.support.fixture.OrderProductFixture;
import shop.woowasap.order.service.support.fixture.ProductFixture;
import shop.woowasap.shop.domain.in.cart.CartConnector;
import shop.woowasap.shop.domain.in.product.ProductConnector;
import shop.woowasap.shop.domain.product.Product;

@ExtendWith(SpringExtension.class)
@DisplayName("OrderConnectorService 클래스")
@ContextConfiguration(classes = OrderConnectorService.class)
class OrderConnectorServiceTest {

    @Autowired
    private OrderConnectorService orderConnectorService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private ProductConnector productConnector;

    @MockBean
    private CartConnector cartConnector;

    @Nested
    @DisplayName("findByOrderIdAndUserId 메소드는")
    class findByOrderIdAndUserIdMethod {

        @Test
        @DisplayName("orderId와 userId를 받아, 일치하는 Order를 반환한다.")
        void returnMatchedOrderWithOrderIdAndUserId() {
            // given
            final long orderId = 1L;
            final long userId = 1L;

            final Product product = ProductFixture.getDefaultBuilder().build();
            final OrderProduct orderProduct = OrderProductFixture.from(product);
            final Order order = OrderFixture.getDefault(List.of(orderProduct));

            when(orderRepository.findOrderByOrderIdAndUserId(orderId, userId))
                .thenReturn(Optional.of(order));

            // when
            final Optional<Order> result = orderConnectorService.findByOrderIdAndUserId(orderId,
                userId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).usingRecursiveComparison().isEqualTo(order);
        }

        @Test
        @DisplayName("orderId와 userId가 일치하는 Order를 찾을 수 없으면, Optional.empty를 반환한다.")
        void returnEmptyOptionalWhenCannotFindMatchedOrder() {
            // given
            final long orderId = 1L;
            final long userId = 1L;

            when(orderRepository.findOrderByOrderIdAndUserId(orderId, userId))
                .thenReturn(Optional.empty());

            // when
            final Optional<Order> result = orderConnectorService.findByOrderIdAndUserId(orderId,
                userId);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("consumeStock 메소드는")
    class consumeStockMethod {

        @Test
        @DisplayName("orderId와 userId를 받아, Order를 Success상태로 변경하고 장바구니를 비운다.")
        void updateOrderTypeSuccess() {
            // given
            final long orderId = 1L;
            final long userId = 2L;

            final Product product = ProductFixture.getDefaultBuilder().build();
            final OrderProduct orderProduct = OrderProductFixture.from(product);
            final Order defaultOrder = OrderFixture.getDefault(List.of(orderProduct));

            when(orderRepository.findOrderByOrderIdAndUserId(orderId, userId)).thenReturn(
                Optional.of(defaultOrder));

            // when
            Exception result = catchException(
                () -> orderConnectorService.consumeStock(orderId, userId));

            // then
            assertThat(result).isNull();
            verify(cartConnector).clearCartByUserId(userId);
        }

        @Test
        @DisplayName("orderId와 userId를 받아, Order의 상태를 Fail로 변경한다")
        void updateOrderTypeFail() {
            // given
            final long orderId = 1L;
            final long userId = 2L;

            final Product product = ProductFixture.getDefaultBuilder().build();
            final OrderProduct orderProduct = OrderProductFixture.from(product);
            final Order defaultOrder = OrderFixture.getDefault(List.of(orderProduct));

            when(orderRepository.findOrderByOrderIdAndUserId(orderId, userId)).thenReturn(
                Optional.of(defaultOrder));
            doThrow(RuntimeException.class).when(productConnector)
                .consumeProductByProductId(product.getId(), product.getQuantity().getValue());

            // when
            Exception result = catchException(
                () -> orderConnectorService.consumeStock(orderId, userId));

            // then
            assertThat(result).isInstanceOf(DoesNotOrderedException.class);
        }

        @Test
        @DisplayName("OrderId에 해당하는 Order를 찾을 수 없는경우, DoesNotFindOrderException을 던진다.")
        void throwDoesNotFindOrderExceptionWhenOrderNotExists() {
            // given
            final long orderId = 1L;
            final long userId = 2L;

            when(orderRepository.findOrderByOrderIdAndUserId(orderId, userId)).thenReturn(
                Optional.empty());

            // when
            Exception result = catchException(
                () -> orderConnectorService.consumeStock(orderId, userId));

            // then
            assertThat(result).isInstanceOf(DoesNotFindOrderException.class);
        }
    }

}
