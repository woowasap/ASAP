package shop.woowasap.accept.support.valid;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import shop.woowasap.mock.dto.ProductsResponse;

public final class ShopValidator {

    private ShopValidator() {
    }

    public static void assertProductRegistered(ExtractableResponse<Response> result) {
        HttpValidator.assertCreated(result);

        assertThat(result.header("Location")).isNotBlank();
    }

    public static void assertProductUpdated(ExtractableResponse<Response> result) {
        HttpValidator.assertOk(result);
    }

    public static void assertProductsFound(ExtractableResponse<Response> result,
        ProductsResponse expected) {
        HttpValidator.assertOk(result);

        ProductsResponse resultResponse = result.as(ProductsResponse.class);
        assertThat(resultResponse).usingRecursiveComparison().ignoringFields("id")
            .isEqualTo(expected);
    }

}
