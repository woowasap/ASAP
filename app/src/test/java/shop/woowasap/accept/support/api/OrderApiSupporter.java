package shop.woowasap.accept.support.api;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpHeaders;
import shop.woowasap.order.controller.request.OrderProductQuantityRequest;


public final class OrderApiSupporter {

    private static final String API_VERSION = "v1";

    private OrderApiSupporter() {
        throw new UnsupportedOperationException(
            "Cannot invoke constructor \"OrderApiSupporter()\"");
    }

    public static ExtractableResponse<Response> orderProduct(final long productId,
        final OrderProductQuantityRequest orderProductRequest, final String token) {

        return given().log().all()
            .contentType(JSON)
            .accept(JSON)
            .header(HttpHeaders.AUTHORIZATION, token)
            .body(orderProductRequest)
            .when().log().all()
            .post(API_VERSION + "/orders/products/{product-id}", productId)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> getAllProducts(final String token) {

        return given().log().all()
            .contentType(JSON)
            .accept(JSON)
            .header(HttpHeaders.AUTHORIZATION, token)
            .when().log().all()
            .get(API_VERSION + "/orders")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> getOrderByOrderId(final long orderId,
        final String token) {

        return given().log().all()
            .contentType(JSON)
            .accept(JSON)
            .header(HttpHeaders.AUTHORIZATION, token)
            .when().log().all()
            .get(API_VERSION + "/orders/{order-id}", orderId)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> orderCart(long cartId, String token) {

        return given().log().all()
            .contentType(JSON)
            .accept(JSON)
            .header(HttpHeaders.AUTHORIZATION, token)
            .when().log().all()
            .post(API_VERSION + "/orders/carts/{cart-id}", cartId)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> cancelOrder(final long orderId, String token) {

        return given().log().all()
            .contentType(JSON)
            .accept(JSON)
            .header(HttpHeaders.AUTHORIZATION, token)
            .when().log().all()
            .post(API_VERSION + "/orders/{order-id}", orderId)
            .then().log().all()
            .extract();
    }
}
