package shop.woowasap.accept;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woowasap.accept.support.api.CartApiSupporter;
import shop.woowasap.accept.support.api.OrderApiSupporter;
import shop.woowasap.accept.support.api.ShopApiSupporter;
import shop.woowasap.accept.support.fixture.ProductFixture;
import shop.woowasap.accept.support.valid.HttpValidator;
import shop.woowasap.accept.support.valid.OrderValidator;
import shop.woowasap.order.controller.request.OrderProductQuantityRequest;
import shop.woowasap.order.domain.in.response.DetailOrderProductResponse;
import shop.woowasap.order.domain.in.response.DetailOrderResponse;
import shop.woowasap.order.domain.in.response.OrderProductResponse;
import shop.woowasap.order.domain.in.response.OrderResponse;
import shop.woowasap.order.domain.in.response.OrdersResponse;
import shop.woowasap.shop.domain.api.cart.request.AddCartProductRequest;
import shop.woowasap.shop.domain.api.cart.response.CartResponse;
import shop.woowasap.shop.domain.api.product.response.ProductResponse;
import shop.woowasap.shop.domain.api.product.response.ProductsResponse;

@DisplayName("Order 인수테스트")
class OrderAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("상품 바로 구매 API는 productId와 quantity를 받아 상품을 구매한후, Created와 Location을 응답한다.")
    void returnCreatedAndLocationWhenSuccessToBuyProduct() {
        // given
        final String token = "TOKEN";

        final long productId = getRandomProduct(token).productId();
        final int quantity = 2;
        final OrderProductQuantityRequest orderProductRequest = new OrderProductQuantityRequest(
            quantity);

        // when
        final ExtractableResponse<Response> result = OrderApiSupporter.orderProduct(productId,
            orderProductRequest,
            token);

        // then
        OrderValidator.assertOrdered(result);
    }

    @Test
    @DisplayName("상품 바로 구매 API는 productId에 해당하는 product를 찾을 수 없는경우, 400 BadRequest를 응답한다.")
    void returnBadRequestWhenCannotFindMatchedProduct() {
        // given
        final String token = "TOKEN";

        final long notExistProductId = 123L;
        final int quantity = 2;
        final OrderProductQuantityRequest orderProductRequest = new OrderProductQuantityRequest(
            quantity);

        // when
        final ExtractableResponse<Response> result = OrderApiSupporter.orderProduct(
            notExistProductId,
            orderProductRequest, token);

        // then
        HttpValidator.assertBadRequest(result);
    }

    @Test
    @DisplayName("구매내역 전체 조회 API는 사용자의 구매내역을 응답한다")
    void returnOrders() {
        // given
        final String token = "TOKEN";
        final ProductResponse product = getRandomProduct(token);
        final long quantity = 10;
        final OrderProductQuantityRequest orderProductQuantityRequest = new OrderProductQuantityRequest(
            quantity);

        OrderApiSupporter.orderProduct(product.productId(), orderProductQuantityRequest, token);

        final OrderProductResponse expectedOrderProductResponse = new OrderProductResponse(
            product.productId(), product.name(), product.price(), quantity);
        final OrderResponse expectedOrderResponse = new OrderResponse(1L,
            List.of(expectedOrderProductResponse),
            new BigInteger(product.price()).multiply(BigInteger.valueOf(quantity)).toString(),
            LocalDateTime.now());
        final OrdersResponse expected = new OrdersResponse(List.of(expectedOrderResponse), 1, 20);

        // when
        final ExtractableResponse<Response> result = OrderApiSupporter.getAllProducts(token);

        // then
        OrderValidator.assertOrders(result, expected);
    }

    @Test
    @DisplayName("특정 구매내역 조회 API는 사용자의 구매내역을 응답한다.")
    void returnDetailOrders() {
        // given
        final String token = "TOKEN";
        final ProductResponse product = getRandomProduct(token);
        final long quantity = 10;
        final OrderProductQuantityRequest orderProductQuantityRequest = new OrderProductQuantityRequest(
            quantity);
        OrderApiSupporter.orderProduct(product.productId(), orderProductQuantityRequest, token);

        final OrdersResponse ordersResponse = OrderApiSupporter.getAllProducts(token)
            .as(OrdersResponse.class);
        final long orderId = ordersResponse.orders().get(0).orderId();

        final DetailOrderProductResponse expectedDetailOrderProductResponse =
            new DetailOrderProductResponse(product.productId(), product.name(), product.price(),
                quantity);
        final DetailOrderResponse expectedDetailOrderResponse = new DetailOrderResponse(orderId,
            List.of(expectedDetailOrderProductResponse),
            new BigInteger(product.price()).multiply(BigInteger.valueOf(quantity)).toString(),
            LocalDateTime.now());

        // when
        final ExtractableResponse<Response> result = OrderApiSupporter.getOrderByOrderId(orderId,
            token);

        // then
        OrderValidator.assertOrder(result, expectedDetailOrderResponse);
    }

    @Test
    @DisplayName("특정 구매내역 조회 API는 orderId에 해당하는 Order를 찾을 수 없는경우 400 Bad Request 를 반환한다.")
    void returnBadRequestWhenCannotFindMatchedOrderByOrderId() {
        // given
        final String token = "TOKEN";
        final long invalidOrderId = 999L;

        // when
        final ExtractableResponse<Response> result = OrderApiSupporter.getOrderByOrderId(
            invalidOrderId, token);

        // then
        HttpValidator.assertBadRequest(result);
    }

    @Test
    @DisplayName("장바구니 상품 구매 API는 장바구니의 상품을 구매한 후, Created와 Location을 응답한다.")
    void returnCreatedAndLocationWhenSuccessToBuyCart() {
        // given
        final String token = "TOKEN";

        final ProductResponse product = getRandomProduct(token);
        final int quantity = 2;
        final AddCartProductRequest addCartProductRequest = new AddCartProductRequest(
            product.productId(), quantity);

        CartApiSupporter.addCartProduct(token, addCartProductRequest);

        final long cartId = CartApiSupporter.getCartProducts(token).as(CartResponse.class).cartId();

        // when
        final ExtractableResponse<Response> result = OrderApiSupporter.orderCart(cartId, token);

        // then
        OrderValidator.assertOrdered(result);
    }

    @Test
    @DisplayName("장바구니 상품 구매 API는 장바구니를 찾을 수 없는경우, 400 BadRequest를 응답한다.")
    void returnBadRequestWhenCannotFindOrder() {
        // given
        final String token = "TOKEN";

        final long invalidCartId = 999L;

        // when
        final ExtractableResponse<Response> result = OrderApiSupporter.orderCart(invalidCartId,
            token);

        // then
        HttpValidator.assertBadRequest(result);
    }

    private ProductResponse getRandomProduct(final String token) {
        ShopApiSupporter.registerProduct(token, ProductFixture.registerValidProductRequest());

        return ShopApiSupporter.getAllProducts().as(ProductsResponse.class)
            .products().get(0);
    }
}
