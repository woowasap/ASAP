package shop.woowasap.accept;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigInteger;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woowasap.accept.support.api.AuthApiSupporter;
import shop.woowasap.accept.support.api.CartApiSupporter;
import shop.woowasap.accept.support.api.OrderApiSupporter;
import shop.woowasap.accept.support.api.PayApiSupporter;
import shop.woowasap.accept.support.api.ShopApiSupporter;
import shop.woowasap.accept.support.fixture.ProductFixture;
import shop.woowasap.accept.support.valid.HttpValidator;
import shop.woowasap.accept.support.valid.OrderValidator;
import shop.woowasap.order.controller.request.OrderProductQuantityRequest;
import shop.woowasap.order.domain.in.response.DetailOrderProductResponse;
import shop.woowasap.order.domain.in.response.DetailOrderResponse;
import shop.woowasap.order.domain.in.response.OrderIdResponse;
import shop.woowasap.order.domain.in.response.OrderProductResponse;
import shop.woowasap.order.domain.in.response.OrderResponse;
import shop.woowasap.order.domain.in.response.OrdersResponse;
import shop.woowasap.payment.controller.request.PayRequest;
import shop.woowasap.shop.domain.in.cart.request.AddCartProductRequest;
import shop.woowasap.shop.domain.in.cart.response.CartResponse;
import shop.woowasap.shop.domain.in.product.response.ProductResponse;
import shop.woowasap.shop.domain.in.product.response.ProductsResponse;

@DisplayName("Order 인수테스트")
class OrderAcceptanceTest extends AcceptanceTest {

    private String accessToken;

    @BeforeEach
    void setAccessToken() {
        accessToken = AuthApiSupporter.adminAccessToken();
    }

    @Test
    @DisplayName("상품 바로 구매 API는 productId와 quantity를 받아 상품을 구매한후, Ok와 OrderIdResponse를 응답한다.")
    void returnOkAndOrderIdResponseWhenSuccessToBuyProduct() {
        // given
        final long productId = getRandomProduct(accessToken).productId();
        final int quantity = 2;
        final OrderProductQuantityRequest orderProductRequest = new OrderProductQuantityRequest(
            quantity);

        timeUtil.clock(Clock.fixed(Instant.now().plus(25, ChronoUnit.MINUTES), ZoneId.of("UTC")));

        // when
        final ExtractableResponse<Response> result = OrderApiSupporter.orderProduct(productId,
            orderProductRequest,
            accessToken);

        // then
        OrderValidator.assertOrdered(result);
    }

    @Test
    @DisplayName("상품 바로 구매 API는 productId에 해당하는 product를 찾을 수 없는경우, 400 BadRequest를 응답한다.")
    void returnBadRequestWhenCannotFindMatchedProduct() {
        // given
        final long notExistProductId = 123L;
        final int quantity = 2;
        final OrderProductQuantityRequest orderProductRequest = new OrderProductQuantityRequest(
            quantity);

        // when
        final ExtractableResponse<Response> result = OrderApiSupporter.orderProduct(
            notExistProductId,
            orderProductRequest, accessToken);

        // then
        HttpValidator.assertBadRequest(result);
    }

    @Test
    @DisplayName("구매내역 전체 조회 API는 사용자의 구매내역을 응답한다")
    void returnOrders() {
        // given
        final ProductResponse product = getRandomProduct(accessToken);
        final long quantity = 10;
        final OrderProductQuantityRequest orderProductQuantityRequest = new OrderProductQuantityRequest(
            quantity);

        timeUtil.clock(Clock.fixed(Instant.now().plus(25, ChronoUnit.MINUTES), ZoneId.of("UTC")));
        OrderApiSupporter.orderProduct(product.productId(), orderProductQuantityRequest,
            accessToken);

        final OrderProductResponse expectedOrderProductResponse = new OrderProductResponse(
            product.productId(), product.name(), product.price(), quantity);
        final OrderResponse expectedOrderResponse = new OrderResponse(1L,
            List.of(expectedOrderProductResponse),
            new BigInteger(product.price()).multiply(BigInteger.valueOf(quantity)).toString(), "PENDING",
            LocalDateTime.now());
        final OrdersResponse expected = new OrdersResponse(List.of(expectedOrderResponse), 1, 1);

        // when
        final ExtractableResponse<Response> result = OrderApiSupporter.getAllProducts(accessToken);

        // then
        OrderValidator.assertOrders(result, expected);
    }

    @Test
    @DisplayName("특정 구매내역 조회 API는 사용자의 구매내역을 응답한다.")
    void returnDetailOrders() {
        // given
        final ProductResponse product = getRandomProduct(accessToken);
        final long quantity = 10;
        final OrderProductQuantityRequest orderProductQuantityRequest = new OrderProductQuantityRequest(
            quantity);

        timeUtil.clock(Clock.fixed(Instant.now().plus(25, ChronoUnit.MINUTES), ZoneId.of("UTC")));
        OrderApiSupporter.orderProduct(product.productId(), orderProductQuantityRequest,
            accessToken);

        final OrdersResponse ordersResponse = OrderApiSupporter.getAllProducts(accessToken)
            .as(OrdersResponse.class);
        final long orderId = ordersResponse.orders().get(0).orderId();

        final DetailOrderProductResponse expectedDetailOrderProductResponse =
            new DetailOrderProductResponse(product.productId(), product.name(), product.price(),
                quantity);
        final DetailOrderResponse expectedDetailOrderResponse = new DetailOrderResponse(orderId,
            List.of(expectedDetailOrderProductResponse),
            new BigInteger(product.price()).multiply(BigInteger.valueOf(quantity)).toString(),
            "PENDING",
            LocalDateTime.now());

        // when
        final ExtractableResponse<Response> result = OrderApiSupporter.getOrderByOrderId(orderId,
            accessToken);

        // then
        OrderValidator.assertOrder(result, expectedDetailOrderResponse);
    }

    @Test
    @DisplayName("특정 구매내역 조회 API는 orderId에 해당하는 Order를 찾을 수 없는경우 400 Bad Request 를 반환한다.")
    void returnBadRequestWhenCannotFindMatchedOrderByOrderId() {
        // given
        final long invalidOrderId = 999L;

        // when
        final ExtractableResponse<Response> result = OrderApiSupporter.getOrderByOrderId(
            invalidOrderId, accessToken);

        // then
        HttpValidator.assertBadRequest(result);
    }

    @Test
    @DisplayName("장바구니 상품 구매 API는 장바구니의 상품을 구매한 후, Ok와 OrderIdResponse를 응답한다.")
    void returnOkAndOrderIdResponseWhenSuccessToBuyCart() {
        // given
        final ProductResponse product = getRandomProduct(accessToken);
        final int quantity = 2;
        final AddCartProductRequest addCartProductRequest = new AddCartProductRequest(
            product.productId(), quantity);

        CartApiSupporter.addCartProduct(accessToken, addCartProductRequest);

        final long cartId = CartApiSupporter.getCartProducts(accessToken).as(CartResponse.class)
            .cartId();

        timeUtil.clock(Clock.fixed(Instant.now().plus(25, ChronoUnit.MINUTES), ZoneId.of("UTC")));

        // when
        final ExtractableResponse<Response> result = OrderApiSupporter.orderCart(cartId,
            accessToken);

        // then
        OrderValidator.assertOrdered(result);
    }

    @Test
    @DisplayName("장바구니 상품 구매 API는 장바구니를 찾을 수 없는경우, 400 BadRequest를 응답한다.")
    void returnBadRequestWhenCannotFindOrder() {
        // given
        final long invalidCartId = 999L;

        // when
        final ExtractableResponse<Response> result = OrderApiSupporter.orderCart(invalidCartId,
            accessToken);

        // then
        HttpValidator.assertBadRequest(result);
    }

    @Test
    @DisplayName("구매 취소 API는 Order의 상태를 canceled로 변경하고, pay의 상태를 canceled로 변경한다.")
    void changePayAndOrderStatusCanceled() {
        // given
        final long productId = getRandomProduct(accessToken).productId();
        final int quantity = 2;
        final OrderProductQuantityRequest orderProductRequest = new OrderProductQuantityRequest(
            quantity);

        timeUtil.clock(Clock.fixed(Instant.now().plus(25, ChronoUnit.MINUTES), ZoneId.of("UTC")));

        final OrderIdResponse orderIdResponse = OrderApiSupporter.orderProduct(productId,
            orderProductRequest,
            accessToken).as(OrderIdResponse.class);

        final PayRequest payRequest = new PayRequest("CARD", true);

        PayApiSupporter.pay(payRequest, orderIdResponse.orderId(), accessToken);

        // when
        final ExtractableResponse<Response> result = OrderApiSupporter.cancelOrder(
            orderIdResponse.orderId(), accessToken);

        // then
        HttpValidator.assertOk(result);
        final DetailOrderResponse detailOrderResponse = OrderApiSupporter.getOrderByOrderId(
                orderIdResponse.orderId(), accessToken)
            .as(DetailOrderResponse.class);
        assertThat(detailOrderResponse.type()).isEqualTo("CANCELED");
    }

    private ProductResponse getRandomProduct(final String accessToken) {
        ShopApiSupporter.registerProduct(accessToken,
            ProductFixture.registerValidProductRequest(Instant.now().plus(20, ChronoUnit.MINUTES),
                Instant.now().plus(60 * 6, ChronoUnit.MINUTES)));

        return ShopApiSupporter.getAllProducts().as(ProductsResponse.class)
            .products().get(0);
    }
}
