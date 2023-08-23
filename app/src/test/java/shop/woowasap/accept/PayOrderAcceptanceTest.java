package shop.woowasap.accept;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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

        timeUtil.clock(Clock.fixed(Instant.now().plus(25, ChronoUnit.MINUTES), ZoneId.of("UTC")));
        final OrderIdResponse orderIdResponse = OrderApiSupporter.orderProduct(productId,
            orderProductRequest,
            accessToken).as(OrderIdResponse.class);

        final PayRequest payRequest = new PayRequest("CARD", true);

        // when
        PayApiSupporter.pay(payRequest, orderIdResponse.orderId(), accessToken);

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

        timeUtil.clock(Clock.fixed(Instant.now().plus(25, ChronoUnit.MINUTES), ZoneId.of("UTC")));
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

    private ProductResponse getRandomProduct(final String accessToken) {
        ShopApiSupporter.registerProduct(accessToken, ProductFixture.registerValidProductRequest(
            Instant.now().plus(20, ChronoUnit.MINUTES),
            Instant.now().plus(60 * 6, ChronoUnit.MINUTES)));

        return ShopApiSupporter.getAllProducts().as(ProductsResponse.class)
            .products().get(0);
    }

}
