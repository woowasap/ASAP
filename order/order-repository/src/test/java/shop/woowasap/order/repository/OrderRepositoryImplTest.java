package shop.woowasap.order.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import shop.woowasap.order.BeanScanBaseLocation;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.OrderType;
import shop.woowasap.order.domain.out.OrderRepository;
import shop.woowasap.order.domain.out.response.OrdersPaginationResponse;
import shop.woowasap.order.repository.support.OrderFixture;

@DataJpaTest
@DisplayName("OrderRepository 클래스")
@ContextConfiguration(classes = {BeanScanBaseLocation.class, OrderRepositoryImpl.class})
class OrderRepositoryImplTest {

    @Autowired
    private OrderRepository orderRepository;

    @Nested
    @DisplayName("persist 메소드는")
    class persistMethod {

        @Test
        @DisplayName("Order를 받아, 업데이트 한다.")
        void saveByOrder() {
            // given
            final Order order = OrderFixture.defaultBuilder()
                .build();

            orderRepository.create(order);
            order.updateOrderType(OrderType.CANCELED);

            // when
            final Exception exception = catchException(() -> orderRepository.persist(order));

            // then
            assertThat(exception).isNull();
        }
    }

    @Nested
    @DisplayName("create 메소드는")
    class createMethod {

        @Test
        @DisplayName("Order를 받아, 저장한다.")
        void saveByOrder() {
            // given
            final Order order = OrderFixture.defaultBuilder()
                .build();

            // when
            final Exception exception = catchException(() -> orderRepository.create(order));

            // then
            assertThat(exception).isNull();
        }
    }

    @Nested
    @DisplayName("findOrderByOrderIdAndUserId 메소드는")
    class findOrderByOrderIdAndUserIdMethod {

        @Test
        @DisplayName("orderId와 userId에 해당하는 Order를 찾을 수 있으면 반환한다.")
        void returnOrderWhenFoundMatchedOrder() {
            // given
            final Order order = OrderFixture.defaultBuilder()
                .build();

            orderRepository.create(order);

            // when
            final Optional<Order> result = orderRepository.findOrderByOrderIdAndUserId(
                order.getId(), order.getUserId());

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).usingRecursiveComparison().isEqualTo(order);
        }

        @Test
        @DisplayName("orderId와 userId에 해당하는 Order를 찾을 수 없으면, Optional.empty를 반환한다.")
        void returnOptionalEmptyWhenCannotFindMatchedOrder() {
            // given
            final long orderId = 1L;
            final long userId = 2L;

            // when
            final Optional<Order> result = orderRepository.findOrderByOrderIdAndUserId(orderId,
                userId);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findAllOrderByUserId 메소드는")
    class findAllOrderByUserIdMethod {

        @Test
        @DisplayName("Order가 2개 저장되어있고 size가 1일때, order가 하나만 있는 OrdersPaginationResponse를 반환한다.")
        void returnOrdersPaginationResponseHasOneOrders() {
            // given
            final Order firstOrder = OrderFixture.defaultBuilder()
                .id(1L)
                .build();
            orderRepository.create(firstOrder);

            final Order secondOrder = OrderFixture.defaultBuilder()
                .id(2L)
                .build();
            orderRepository.create(secondOrder);

            final int page = 1;
            final int size = 1;

            final OrdersPaginationResponse expected = new OrdersPaginationResponse(
                List.of(firstOrder), page, 2);

            // when
            final OrdersPaginationResponse result = orderRepository.findAllOrderByUserId(
                firstOrder.getUserId(), page, size);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        @DisplayName("Order가 2개 저장되어있고 size가 2일때, order 2개가 저장되어있는 OrdersPaginationResponse를 반환한다.")
        void returnOrdersPaginationResponseHasTwoOrders() {
            // given
            final Order firstOrder = OrderFixture.defaultBuilder()
                .id(1L)
                .build();
            orderRepository.create(firstOrder);

            final Order secondOrder = OrderFixture.defaultBuilder()
                .id(2L)
                .build();
            orderRepository.create(secondOrder);

            final int page = 1;
            final int size = 2;

            final OrdersPaginationResponse expected = new OrdersPaginationResponse(
                List.of(firstOrder, secondOrder), page, 1);

            // when
            final OrdersPaginationResponse result = orderRepository.findAllOrderByUserId(
                firstOrder.getUserId(), page, size);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        }
    }
}
