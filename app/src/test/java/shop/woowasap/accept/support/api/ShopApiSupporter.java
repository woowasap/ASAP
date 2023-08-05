package shop.woowasap.accept.support.api;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpHeaders;
import shop.woowasap.mock.dto.RegisterProductRequest;
import shop.woowasap.mock.dto.UpdateProductRequest;

public final class ShopApiSupporter {

    private static final String API_VERSION = "/v1";

    private ShopApiSupporter() {
    }

    public static ExtractableResponse<Response> registerProduct(String token,
        RegisterProductRequest registerProductRequest) {

        return given().log().all()
            .contentType(JSON)
            .accept(JSON)
            .header(HttpHeaders.AUTHORIZATION, token)
            .body(registerProductRequest)
            .when().log().all()
            .post(API_VERSION + "/products")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> updateProduct(String token,
        long productId,
        UpdateProductRequest updateProductRequest) {

        return given().log().all()
            .contentType(JSON)
            .accept(JSON)
            .header(HttpHeaders.AUTHORIZATION, token)
            .body(updateProductRequest)
            .when().log().all()
            .put(API_VERSION + "/products/{product-id}", productId)
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

    public static ExtractableResponse<Response> getRegisteredProducts(String token, long userId) {
        return given().log().all()
            .accept(JSON)
            .when().log().all()
            .get(API_VERSION + "/products/{user-id}", userId)
            .then().log().all()
            .extract();
    }

}
