package shop.woowasap.accept.support.valid;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpHeaders;
import shop.woowasap.order.domain.in.response.OrdersResponse;

public final class OrderValidator {

    private OrderValidator() {
        throw new UnsupportedOperationException("Cannot invoke constructor \"OrderValidator()\"");
    }

    public static void assertOrdered(ExtractableResponse<Response> result) {
        HttpValidator.assertCreated(result);

        assertThat(result.header(HttpHeaders.LOCATION)).contains("/v1/orders/");
    }

    public static void assertOrders(ExtractableResponse<Response> result, OrdersResponse expected) {
        HttpValidator.assertOk(result);

        OrdersResponse ordersResult = result.as(OrdersResponse.class);

        assertThat(ordersResult).usingRecursiveAssertion()
            .ignoringFields("orders.createdAt", "orders.orderId")
            .isEqualTo(expected);
    }
}
