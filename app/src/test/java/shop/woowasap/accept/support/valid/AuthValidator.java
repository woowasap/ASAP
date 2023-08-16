package shop.woowasap.accept.support.valid;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public final class AuthValidator {

    private AuthValidator() {
    }

    public static void assertLoginSuccess(ExtractableResponse<Response> result) {
        HttpValidator.assertOk(result);

        String token = result.jsonPath().getString("accessToken");
        assertThat(token).isNotBlank();
    }

    public static void assertLoginFail(ExtractableResponse<Response> result) {
        HttpValidator.assertBadRequest(result);
    }
}
