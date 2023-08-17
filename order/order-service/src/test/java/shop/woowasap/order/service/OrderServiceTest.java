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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import shop.woowasap.core.id.api.IdGenerator;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.OrderProduct;
import shop.woowasap.order.domain.exception.DoesNotFindCartException;
import shop.woowasap.order.domain.exception.DoesNotFindOrderException;
import shop.woowasap.order.domain.exception.DoesNotFindProductException;
import shop.woowasap.order.domain.exception.DoesNotOrderedException;
import shop.woowasap.order.domain.in.OrderUseCase;
import shop.woowasap.order.domain.in.request.OrderProductRequest;
import shop.woowasap.order.domain.in.response.DetailOrderResponse;
import shop.woowasap.order.domain.in.response.OrdersResponse;
import shop.woowasap.order.domain.out.OrderRepository;
import shop.woowasap.order.domain.out.Payment;
import shop.woowasap.order.domain.out.response.OrdersPaginationResponse;
import shop.woowasap.order.service.support.fixture.CartFixture;
import shop.woowasap.order.service.support.fixture.OrderDtoFixture;
import shop.woowasap.order.service.support.fixture.OrderFixture;
import shop.woowasap.order.service.support.fixture.OrderProductFixture;
import shop.woowasap.order.service.support.fixture.ProductFixture;
import shop.woowasap.shop.domain.in.cart.CartConnector;
import shop.woowasap.shop.domain.in.product.ProductConnector;
import shop.woowasap.shop.domain.cart.Cart;
import shop.woowasap.shop.domain.cart.CartProduct;
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
    private CartConnector cartConnector;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private Payment payment;

    @Nested
    @DisplayName("orderProduct 메소드는")
    class orderProductMethod {

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
    @DisplayName("orderCartByCartIdAndUserId 메소드는")
    class orderCartByCartIdAndUserIdMethod {

        @Test
        @DisplayName("cartId와 userId를 받아, 주문에 성공하면, cartId를 반환한다.")
        void returnCartIdWhenOrderSuccess() {
            // given
            final long cartId = 2L;
            final long userId = 1L;
            final long orderId = 3L;

            final CartProduct cartProduct = CartFixture.getCartProductBuilder().build();
            final Cart cart = CartFixture.getEmptyCartBuilder()
                .id(cartId)
                .userId(userId)
                .cartProducts(List.of(cartProduct))
                .build();

            when(cartConnector.findByCartIdAndUserId(cartId, userId)).thenReturn(Optional.of(cart));
            when(idGenerator.generate()).thenReturn(orderId);
            when(payment.pay(userId)).thenReturn(true);

            // when
            final long result = orderUseCase.orderCartByCartIdAndUserId(cartId, userId);

            // then
            assertThat(result).isEqualTo(orderId);
        }

        @Test
        @DisplayName("cartId와 userId에 해당하는 Cart를 찾을 수 없으면, DoesNotFindCartException을 던진다.")
        void throwDoesNotFindCartExceptionWhenCannotFindMatchedCart() {
            // given
            final long cartId = 2L;
            final long userId = 1L;

            when(cartConnector.findByCartIdAndUserId(cartId, userId)).thenReturn(Optional.empty());

            // when
            final Exception result = catchException(
                () -> orderUseCase.orderCartByCartIdAndUserId(cartId, userId));

            // then
            assertThat(result).isInstanceOf(DoesNotFindCartException.class);
        }

        @Test
        @DisplayName("주문을 실패하면, DoesNotOrderedException을 던진다.")
        void throwDoesNotOrderedExceptionWhenFailOrder() {
            // given
            final long cartId = 2L;
            final long userId = 1L;
            final long orderId = 3L;

            final CartProduct cartProduct = CartFixture.getCartProductBuilder().build();
            final Cart cart = CartFixture.getEmptyCartBuilder()
                .id(cartId)
                .userId(userId)
                .cartProducts(List.of(cartProduct))
                .build();

            when(cartConnector.findByCartIdAndUserId(cartId, userId)).thenReturn(Optional.of(cart));
            when(idGenerator.generate()).thenReturn(orderId);
            when(payment.pay(userId)).thenReturn(false);

            // when
            final Exception result = catchException(
                () -> orderUseCase.orderCartByCartIdAndUserId(cartId, userId));

            // then
            assertThat(result).isInstanceOf(DoesNotOrderedException.class);
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

            final OrdersResponse expected = OrderDtoFixture.ordersResponse(List.of(defaultOrder),
                "Asia/Seoul", page, size, product.getName().getValue(),
                product.getPrice().getValue().toString());

            // when
            final OrdersResponse result = orderUseCase.getOrderByUserId(userId, page, size);

            // then
            assertThat(result).isEqualTo(expected);
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

    @Nested
    @DisplayName("getOrderByOrderIdAndUserId 메소드는")
    class getOrderByOrderIdAndUserIdMethod {

        @Test
        @DisplayName("orderId와 userId를 받아, DetailOrderResponse 를 반환한다.")
        void returnDetailOrderResponseWhenReceiveOrderIdAndUserId() {
            // given
            final long orderId = 1L;
            final long userId = 1L;

            final Product product = ProductFixture.getDefaultBuilder().build();
            final OrderProduct orderProduct = OrderProductFixture.from(product);
            final Order order = OrderFixture.getDefault(List.of(orderProduct));

            when(orderRepository.findOrderByOrderIdAndUserId(orderId, userId)).thenReturn(
                Optional.of(order));

            when(productConnector.findByProductId(product.getId())).thenReturn(
                Optional.of(product));

            final DetailOrderResponse expected = OrderDtoFixture.detailOrderResponse(order,
                product.getName().getValue(),
                product.getPrice().getValue().toString(), "Asia/Seoul");

            // when
            final DetailOrderResponse result = orderUseCase.getOrderByOrderIdAndUserId(orderId,
                userId);

            // then
            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("orderId와 userId에 해당하는 Order를 찾을 수 없을경우, DoesNotFindOrderException을 던진다.")
        void throwDoesNotFindOrderExceptionWhenCannotFindMatchedOrder() {
            // given
            final long orderId = 1L;
            final long userId = 1L;

            when(orderRepository.findOrderByOrderIdAndUserId(orderId, userId)).thenReturn(
                Optional.empty());

            // when
            final Exception result = catchException(
                () -> orderUseCase.getOrderByOrderIdAndUserId(orderId, userId));

            // then
            assertThat(result).isInstanceOf(DoesNotFindOrderException.class);
        }
    }
}
