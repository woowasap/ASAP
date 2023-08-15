package shop.woowasap.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import shop.woowasap.core.id.api.IdGenerator;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.OrderProduct;
import shop.woowasap.order.domain.exception.DoesNotFindProductException;
import shop.woowasap.order.domain.exception.DoesNotOrderedException;
import shop.woowasap.order.domain.in.OrderUseCase;
import shop.woowasap.order.domain.in.request.OrderProductRequest;
import shop.woowasap.order.domain.in.response.OrdersResponse;
import shop.woowasap.order.domain.out.OrderRepository;
import shop.woowasap.order.domain.out.Payment;
import shop.woowasap.order.domain.out.response.OrdersPaginationResponse;
import shop.woowasap.order.service.support.fixture.OrderDtoFixture;
import shop.woowasap.order.service.support.fixture.OrderFixture;
import shop.woowasap.order.service.support.fixture.OrderProductFixture;
import shop.woowasap.order.service.support.fixture.ProductFixture;
import shop.woowasap.shop.domain.api.product.ProductConnector;
import shop.woowasap.shop.domain.product.Product;

@DisplayName("OrderService 클래스")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = OrderService.class)
class OrderServiceTest {

    @Autowired
    private OrderUseCase orderUseCase;

    @MockBean
    private IdGenerator idGenerator;

    @MockBean
    private ProductConnector productConnector;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private Payment payment;

    @Nested
    @DisplayName("order 메소드는")
    class orderMethod {

        @Test
        @DisplayName("userId, productId, OrderProductRequest 를 받아 주문을 한다.")
        void successOrderWhenReceiveUserIdAndOrderProductRequest() {
            // given
            final long userId = 1L;
            final long productId = 2L;
            final int quantity = 3;
            final OrderProductRequest orderProductRequest = new OrderProductRequest(userId,
                productId, quantity);

            final long orderId = 1L;
            when(idGenerator.generate()).thenReturn(orderId);
            when(productConnector.findByProductId(productId)).thenReturn(
                Optional.of(ProductFixture.getDefaultBuilder()
                    .build()));
            when(payment.pay(userId)).thenReturn(true);

            // when
            final long result = orderUseCase.orderProduct(orderProductRequest);

            // then
            assertThat(result).isEqualTo(orderId);
        }

        @Test
        @DisplayName("주문을 실패하면, DoesNotOrderedException을 던진다.")
        void throwDoesNotOrderedExceptionWhenFailToOrder() {
            // given
            final long userId = 1L;
            final long productId = 2L;
            final int quantity = 3;
            final OrderProductRequest orderProductRequest = new OrderProductRequest(userId,
                productId, quantity);

            final long orderId = 1L;
            when(idGenerator.generate()).thenReturn(orderId);
            when(productConnector.findByProductId(productId)).thenReturn(
                Optional.of(ProductFixture.getDefaultBuilder()
                    .build()));
            when(payment.pay(userId)).thenReturn(false);

            // when
            final Exception exception = catchException(
                () -> orderUseCase.orderProduct(orderProductRequest));

            // then
            assertThat(exception).isInstanceOf(DoesNotOrderedException.class);
        }

        @Test
        @DisplayName("productId에 해당하는 Product를 찾을 수 없으면 DoesNotFindProductException을 던진다.")
        void throwDoesNotFindProductExceptionWhenCannotFindMatchedProduct() {
            // given
            final long userId = 1L;
            final long productId = 2L;
            final int quantity = 3;
            final OrderProductRequest orderProductRequest = new OrderProductRequest(userId,
                productId, quantity);

            when(productConnector.findByProductId(productId)).thenReturn(Optional.empty());

            // when
            final Exception exception = catchException(
                () -> orderUseCase.orderProduct(orderProductRequest));

            // then
            assertThat(exception).isInstanceOf(DoesNotFindProductException.class);
        }
    }

    @Nested
    @DisplayName("getOrderByUserId 메소드는")
    class getOrderByUserIdMethod {

        @Test
        @DisplayName("userId와 page, totalPage에 해당하는 모든 주문목록을 조회한다.")
        void returnAllOrdersResponseMatchedUserIdAndPageAndTotalPage() {
            // given
            final long userId = 1L;
            final int page = 1;
            final int size = 1;

            final Product product = ProductFixture.getDefaultBuilder().build();
            final OrderProduct orderProduct = OrderProductFixture.from(product);
            final Order defaultOrder = OrderFixture.getDefault(List.of(orderProduct));

            when(orderRepository.findAllOrderByUserId(userId, page, size)).thenReturn(
                new OrdersPaginationResponse(List.of(defaultOrder), page, size));

            when(productConnector.findByProductId(product.getId())).thenReturn(
                Optional.of(product));

            final OrdersResponse expected = OrderDtoFixture.from(List.of(defaultOrder), "Asia/Seoul",
                page, size, product.getName().getValue());

            // when
            final OrdersResponse result = orderUseCase.getOrderByUserId(userId, page, size);

            // then
            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("productId에 해당하는 product를 찾을 수 없으면 DoesNotFindProductException를 던진다.")
        void throwDoesNotFindProductExceptionWhenCannotFindMatchedProduct() {
            // given
            final long userId = 1L;
            final int page = 1;
            final int size = 1;

            final Product product = ProductFixture.getDefaultBuilder().build();
            final OrderProduct orderProduct = OrderProductFixture.from(product);
            final Order defaultOrder = OrderFixture.getDefault(List.of(orderProduct));

            when(orderRepository.findAllOrderByUserId(userId, page, size)).thenReturn(
                new OrdersPaginationResponse(List.of(defaultOrder), page, size));

            when(productConnector.findByProductId(Mockito.anyLong())).thenReturn(Optional.empty());

            // when
            final Exception result = catchException(
                () -> orderUseCase.getOrderByUserId(userId, page, size));

            // then
            assertThat(result).isInstanceOf(DoesNotFindProductException.class);
        }

        @Test
        @DisplayName("userId에 해당하는 user가 주문한 상품이 없다면 빈 Order가 반환된다.")
        void returnEmptyOrdersWhenNoExistOrderHistory() {
            // given
            final long userId = 1L;
            final int page = 1;
            final int size = 1;

            when(orderRepository.findAllOrderByUserId(userId, page, size)).thenReturn(
                new OrdersPaginationResponse(List.of(), page, size));

            // when
            final OrdersResponse result = orderUseCase.getOrderByUserId(userId, page, size);

            // then
            assertThat(result.orders()).isEmpty();
        }
    }
}
