package shop.woowasap.accept.support.api;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import shop.woowasap.auth.controller.request.LoginRequest;
import shop.woowasap.auth.controller.request.SignUpRequest;

public final class AuthApiSupporter {

    private static final String API_VERSION = "/v1";

    private AuthApiSupporter() {
    }

    public static ExtractableResponse<Response> login(LoginRequest loginRequest) {
        return given().log().all()
            .contentType(JSON)
            .accept(JSON)
            .body(loginRequest)
            .when().log().all()
            .post(API_VERSION + "/login")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> signUp(SignUpRequest signUpRequest) {
        return given().log().all()
            .contentType(JSON)
            .accept(JSON)
            .body(signUpRequest)
            .when().log().all()
            .post(API_VERSION + "/signup")
            .then().log().all()
            .extract();
    }

}
