package shop.woowasap.accept.support.valid;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpHeaders;

public final class OrderValidator {

    private OrderValidator() {
        throw new UnsupportedOperationException("Cannot invoke constructor \"OrderValidator()\"");
    }

    public static void assertOrdered(ExtractableResponse<Response> result) {
        HttpValidator.assertCreated(result);

        assertThat(result.header(HttpHeaders.AUTHORIZATION)).contains("/v1/orders/");
    }
}
