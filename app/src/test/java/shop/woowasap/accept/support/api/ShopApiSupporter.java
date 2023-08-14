package shop.woowasap.accept.support.api;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpHeaders;
import shop.woowasap.shop.domain.api.product.request.RegisterProductRequest;
import shop.woowasap.shop.domain.api.product.request.UpdateProductRequest;

public final class ShopApiSupporter {

    private static final String API_VERSION = "/v1";

    private ShopApiSupporter() {
    }

    public static ExtractableResponse<Response> registerProduct(final String token,
        final RegisterProductRequest registerProductRequest) {

        return given().log().all()
            .contentType(JSON)
            .accept(JSON)
            .header(HttpHeaders.AUTHORIZATION, token)
            .body(registerProductRequest)
            .when().log().all()
            .post(API_VERSION + "/admin/products")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> updateProduct(final String token,
        final long productId,
        final UpdateProductRequest updateProductRequest) {

        return given().log().all()
            .contentType(JSON)
            .accept(JSON)
            .header(HttpHeaders.AUTHORIZATION, token)
            .body(updateProductRequest)
            .when().log().all()
            .put(API_VERSION + "/admin/products/{product-id}", productId)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> getAllProducts() {
        return given().log().all()
            .accept(JSON)
            .when().log().all()
            .get(API_VERSION + "/products")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> getRegisteredProducts(final String token) {
        return given().log().all()
            .accept(JSON)
            .header(HttpHeaders.AUTHORIZATION, token)
            .when().log().all()
            .get(API_VERSION + "/admin/products")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> getProduct(final long productId) {
        return given().log().all()
            .accept(JSON)
            .when().log().all()
            .get(API_VERSION + "/products/{product-id}", productId)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> getProductWithAdmin(final String token,
        final long productId) {
        return given().log().all()
            .accept(JSON)
            .header(HttpHeaders.AUTHORIZATION, token)
            .when().log().all()
            .get(API_VERSION + "/admin/products/{product-id}", productId)
            .then().log().all()
            .extract();
    }
}
