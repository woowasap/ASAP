package shop.woowasap.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import shop.woowasap.order.domain.exception.InvalidOrderProductException;
import shop.woowasap.order.domain.support.fixture.OrderFixture;
import shop.woowasap.order.domain.support.fixture.OrderProductFixture;

@DisplayName("Order 클래스")
class OrderTest {

    @Nested
    @DisplayName("new constructor 는")
    class newConstructor {

        @Test
        @DisplayName("상품이 하나 이상 있을때 생성에 성공한다")
        void createSuccessWhenMoreThanOneProduct() {
            // given
            final OrderProduct orderProduct = OrderProductFixture.defaultProduct();
            final Order.OrderBuilder orderBuilder = OrderFixture.defaultBuilder();
            final Order expected = OrderFixture.defaultBuilder()
                    .orderProducts(List.of(orderProduct))
                    .build();

            // when
            final Order order = orderBuilder.orderProducts(List.of(orderProduct))
                    .build();

            // then
            assertThat(order).usingRecursiveComparison()
                .ignoringFields("createdAt")
                .isEqualTo(expected);
        }

        @Test
        @DisplayName("상품이 0개 이하일때, InvalidOrderProductException을 던진다.")
        void throwInvalidOrderProductExceptionWhenProductCountZero() {
            // given
            final List<OrderProduct> emptyOrderProducts = Collections.emptyList();
            final Order.OrderBuilder orderBuilder = OrderFixture.defaultBuilder();

            // when
            final Exception exception = catchException(() -> orderBuilder.orderProducts(emptyOrderProducts)
                    .build());

            // then
            assertThat(exception).isInstanceOf(InvalidOrderProductException.class);
        }

        @Test
        @DisplayName("상품이 null 일때, InvalidOrderProductException을 던진다.")
        void throwInvalidProductExceptionWhenOrderProductsWasNull() {
            // given
            final List<OrderProduct> nullOrderProducts = null;
            final Order.OrderBuilder orderBuilder = OrderFixture.defaultBuilder();

            // when
            final Exception exception = catchException(() -> orderBuilder.orderProducts(nullOrderProducts)
                    .build());

            // then
            assertThat(exception).isInstanceOf(InvalidOrderProductException.class);
        }
    }

    @Nested
    @DisplayName("updateOrderType 메서드는")
    class updateOrderType {

        @Test
        @DisplayName("orderType 을 변경할 수 있다.")
        void createSuccessWhenMoreThanOneProduct() {
            // given
            final OrderProduct orderProduct = OrderProductFixture.defaultProduct();
            final Order.OrderBuilder orderBuilder = OrderFixture.defaultBuilder();
            final OrderType expected = OrderType.SUCCESS;
            final Order order = orderBuilder.orderProducts(List.of(orderProduct))
                .build();

            // when
            final Order updatedOrderType = order.updateOrderType(expected);

            // then
            assertThat(updatedOrderType.getOrderType()).isEqualTo(expected);
        }
    }
}
