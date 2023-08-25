package shop.woowasap.accept.support.api;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpHeaders;
import shop.woowasap.payment.controller.request.PayRequest;

public final class PayApiSupporter {

    private static final String API_VERSION = "v1";

    private PayApiSupporter() {
        throw new UnsupportedOperationException(
            "Cannot invoke constructor \"PayApiSupporter()\"");
    }

    public static ExtractableResponse<Response> pay(final PayRequest request, final long orderId, final String token) {

        return given().log().all()
            .contentType(JSON)
            .accept(JSON)
            .header(HttpHeaders.AUTHORIZATION, token)
            .body(request)
            .when().log().all()
            .post(API_VERSION + "/pays/{order-id}", orderId)
            .then().log().all()
            .extract();
    }
}
