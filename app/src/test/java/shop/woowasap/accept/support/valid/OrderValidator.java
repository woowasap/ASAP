package shop.woowasap.accept.support.valid;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpHeaders;
import shop.woowasap.order.domain.in.response.DetailOrderResponse;
import shop.woowasap.order.domain.in.response.OrderIdResponse;
import shop.woowasap.order.domain.in.response.OrdersResponse;

public final class OrderValidator {

    private OrderValidator() {
        throw new UnsupportedOperationException("Cannot invoke constructor \"OrderValidator()\"");
    }

    public static void assertOrdered(ExtractableResponse<Response> result) {
        HttpValidator.assertOk(result);

        OrderIdResponse resultResponse = result.as(OrderIdResponse.class);

        assertThat(resultResponse.orderId()).isPositive();
    }

    public static void assertOrders(ExtractableResponse<Response> result, OrdersResponse expected) {
        HttpValidator.assertOk(result);

        OrdersResponse ordersResult = result.as(OrdersResponse.class);

        assertThat(ordersResult).usingRecursiveComparison()
            .ignoringFields("orders.createdAt", "orders.orderId")
            .isEqualTo(expected);
    }

    public static void assertOrder(ExtractableResponse<Response> result,
        DetailOrderResponse expected) {

        HttpValidator.assertOk(result);

        DetailOrderResponse detailOrderResult = result.as(DetailOrderResponse.class);

        assertThat(detailOrderResult).usingRecursiveComparison()
            .ignoringFields("createdAt")
            .isEqualTo(expected);
    }
}
