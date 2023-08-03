package shop.woowasap.accept.support.valid;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import shop.woowasap.mock.dto.SignUpResponse;

public final class AuthValidator {

    private AuthValidator() {
    }

    public static void assertLogin(ExtractableResponse<Response> result) {
        HttpValidator.assertOk(result);

        String token = result.jsonPath().getString("token");
        assertThat(token).isNotBlank();
    }

    public static void assertSigned(ExtractableResponse<Response> result, SignUpResponse expected) {
        HttpValidator.assertCreated(result);

        SignUpResponse resultResponse = result.as(SignUpResponse.class);
        assertThat(resultResponse).isEqualTo(expected);
    }
}
