package shop.woowasap.accept;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woowasap.accept.support.api.AuthApiSupporter;
import shop.woowasap.accept.support.api.OrderApiSupporter;
import shop.woowasap.accept.support.api.PayApiSupporter;
import shop.woowasap.accept.support.api.ShopApiSupporter;
import shop.woowasap.accept.support.fixture.ProductFixture;
import shop.woowasap.order.controller.request.OrderProductQuantityRequest;
import shop.woowasap.order.domain.in.response.DetailOrderResponse;
import shop.woowasap.order.domain.in.response.OrderIdResponse;
import shop.woowasap.payment.controller.request.PayRequest;
import shop.woowasap.shop.domain.in.product.response.ProductDetailsResponse;
import shop.woowasap.shop.domain.in.product.response.ProductResponse;
import shop.woowasap.shop.domain.in.product.response.ProductsResponse;

@DisplayName("Pay Order 인수테스트")
class PayOrderAcceptanceTest extends AcceptanceTest {

    private String accessToken;

    @BeforeEach
    void setAccessToken() {
        accessToken = AuthApiSupporter.adminAccessToken();
    }

    @Test
    @DisplayName("결제에 성공하면, Product quantity가 줄어들고, Order의 상태가 SUCCESS로 변경된다")
    void decreaseProductQuantityWhenPaySuccess() {
        // given
        final long productId = getRandomProduct(accessToken).productId();
        final int quantity = 2;
        final OrderProductQuantityRequest orderProductRequest = new OrderProductQuantityRequest(
            quantity);
        final OrderIdResponse orderIdResponse = OrderApiSupporter.orderProduct(productId,
            orderProductRequest,
            accessToken).as(OrderIdResponse.class);

        final PayRequest payRequest = new PayRequest("CARD", true);

        // when
        PayApiSupporter.pay(payRequest, orderIdResponse.orderId(), accessToken);

        Awaitility.waitAtMost(1, TimeUnit.MINUTES)
            .until(() -> waitUntilSuccess(orderIdResponse));

        // then
        final ProductDetailsResponse productResponse = ShopApiSupporter.getProduct(productId)
            .as(ProductDetailsResponse.class);
        final DetailOrderResponse orderResponse = OrderApiSupporter.getOrderByOrderId(orderIdResponse.orderId(),
                accessToken)
            .as(DetailOrderResponse.class);

        assertThat(productResponse.quantity()).isEqualTo(8);
        assertThat(orderResponse.type()).isEqualTo("SUCCESS");
    }

    @Test
    @DisplayName("결제에 실패하면, Product quantity가 그대로 남고, Order의 상태가 PENDING상태가 유지된다.")
    void payFailOrderStatusPending() {
        // given
        final long productId = getRandomProduct(accessToken).productId();
        final int quantity = 2;
        final OrderProductQuantityRequest orderProductRequest = new OrderProductQuantityRequest(
            quantity);
        final OrderIdResponse orderIdResponse = OrderApiSupporter.orderProduct(productId,
            orderProductRequest,
            accessToken).as(OrderIdResponse.class);

        final PayRequest payRequest = new PayRequest("CARD", false);

        // when
        PayApiSupporter.pay(payRequest, orderIdResponse.orderId(), accessToken);

        // then
        final ProductDetailsResponse productResponse = ShopApiSupporter.getProduct(productId)
            .as(ProductDetailsResponse.class);
        final DetailOrderResponse orderResponse = OrderApiSupporter.getOrderByOrderId(orderIdResponse.orderId(),
                accessToken)
            .as(DetailOrderResponse.class);

        assertThat(productResponse.quantity()).isEqualTo(10);
        assertThat(orderResponse.type()).isEqualTo("PENDING");
    }

    @Test
    @DisplayName("결제 실패 후, 재결재를 하면, Product quantity가 줄어들고, Order의 상태가 SUCCESS로 변경된다")
    void decreaseProductQuantityWhenPayReSuccess() throws Exception {
        // given
        final long productId = getRandomProduct(accessToken).productId();
        final int quantity = 2;
        final OrderProductQuantityRequest orderProductRequest = new OrderProductQuantityRequest(
            quantity);
        final OrderIdResponse orderIdResponse = OrderApiSupporter.orderProduct(productId,
            orderProductRequest,
            accessToken).as(OrderIdResponse.class);

        final PayRequest payFailRequest = new PayRequest("CARD", false);
        final PayRequest paySuccessRequest = new PayRequest("CARD", true);

        // when
        PayApiSupporter.pay(payFailRequest, orderIdResponse.orderId(), accessToken);
        PayApiSupporter.pay(paySuccessRequest, orderIdResponse.orderId(), accessToken);

        Awaitility.waitAtMost(1, TimeUnit.MINUTES)
            .until(() -> waitUntilSuccess(orderIdResponse));

        // then
        final ProductDetailsResponse productResponse = ShopApiSupporter.getProduct(productId)
            .as(ProductDetailsResponse.class);
        final DetailOrderResponse orderResponse = OrderApiSupporter.getOrderByOrderId(orderIdResponse.orderId(),
                accessToken)
            .as(DetailOrderResponse.class);

        assertThat(productResponse.quantity()).isEqualTo(8);
        assertThat(orderResponse.type()).isEqualTo("SUCCESS");
    }

    private boolean waitUntilSuccess(final OrderIdResponse orderIdResponse) {
        final DetailOrderResponse orderResponse = OrderApiSupporter.getOrderByOrderId(orderIdResponse.orderId(),
                accessToken)
            .as(DetailOrderResponse.class);

        return orderResponse.type().equals("SUCCESS");
    }

    private ProductResponse getRandomProduct(final String accessToken) {
        ShopApiSupporter.registerProduct(accessToken, ProductFixture.registerValidProductRequest());

        return ShopApiSupporter.getAllProducts().as(ProductsResponse.class)
            .products().get(0);
    }

}
