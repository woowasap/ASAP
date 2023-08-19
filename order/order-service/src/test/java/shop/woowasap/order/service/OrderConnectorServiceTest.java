package shop.woowasap.order.service;

import static org.assertj.core.api.Assertions.assertThat;
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
import shop.woowasap.order.domain.out.OrderRepository;
import shop.woowasap.order.service.support.fixture.OrderFixture;
import shop.woowasap.order.service.support.fixture.OrderProductFixture;
import shop.woowasap.order.service.support.fixture.ProductFixture;
import shop.woowasap.shop.domain.product.Product;

@ExtendWith(SpringExtension.class)
@DisplayName("OrderConnectorService 클래스")
@ContextConfiguration(classes = OrderConnectorService.class)
class OrderConnectorServiceTest {

    @Autowired
    private OrderConnectorService orderConnectorService;

    @MockBean
    private OrderRepository orderRepository;

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
            final Optional<Order> result = orderConnectorService.findByOrderIdAndUserId(orderId, userId);

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
            final Optional<Order> result = orderConnectorService.findByOrderIdAndUserId(orderId, userId);

            // then
            assertThat(result).isEmpty();
        }
    }

}
