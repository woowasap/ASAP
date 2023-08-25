package shop.woowasap.accept.support.api;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpHeaders;
import shop.woowasap.shop.domain.in.cart.request.AddCartProductRequest;
import shop.woowasap.shop.domain.in.cart.request.UpdateCartProductRequest;

public final class CartApiSupporter {

    private static final String API_VERSION = "/v1";

    private CartApiSupporter() {
    }

    public static ExtractableResponse<Response> addCartProduct(final String token,
        final AddCartProductRequest addCartProductRequest) {

        return given().log().all()
            .contentType(JSON)
            .accept(JSON)
            .header(HttpHeaders.AUTHORIZATION, token)
            .body(addCartProductRequest)
            .when().log().all()
            .post(API_VERSION + "/carts")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> updateCartProduct(final String token,
        final UpdateCartProductRequest updateCartProductRequest) {

        return given().log().all()
            .contentType(JSON)
            .accept(JSON)
            .header(HttpHeaders.AUTHORIZATION, token)
            .body(updateCartProductRequest)
            .when().log().all()
            .patch(API_VERSION + "/carts")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> getCartProducts(final String token) {
        return given().log().all()
            .accept(JSON)
            .header(HttpHeaders.AUTHORIZATION, token)
            .when().log().all()
            .get(API_VERSION + "/carts")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> deleteCartProduct(final String token,
        final long productId) {
        return given().log().all()
            .accept(JSON)
            .header(HttpHeaders.AUTHORIZATION, token)
            .queryParam("product-id", productId)
            .when().log().all()
            .delete(API_VERSION + "/carts")
            .then().log().all()
            .extract();
    }
}
