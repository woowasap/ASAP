package shop.woowasap.accept.support.valid;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

public final class HttpValidator {

    private HttpValidator() {
    }

    public static void assertOk(ExtractableResponse<Response> result) {
        assertThat(result.statusCode()).isEqualTo(HttpStatus.SC_OK);
    }

    public static void assertCreated(ExtractableResponse<Response> result) {
        assertThat(result.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
    }

    public static void assertBadRequest(ExtractableResponse<Response> result) {
        assertThat(result.statusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
    }

    public static void assertForbidden(ExtractableResponse<Response> result) {
        assertThat(result.statusCode()).isEqualTo(HttpStatus.SC_FORBIDDEN);
    }

    public static void assertConflict(ExtractableResponse<Response> result) {
        assertThat(result.statusCode()).isEqualTo(HttpStatus.SC_CONFLICT);
    }
}
