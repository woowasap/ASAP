package shop.woowasap.accept.support.valid;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import shop.woowasap.shop.app.api.response.ProductsResponse;

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
        assertProducts(resultResponse, expected);
    }

    private static void assertProducts(ProductsResponse result, ProductsResponse expected) {
        assertThat(result.page()).isEqualTo(expected.page());
        assertThat(result.totalPage()).isEqualTo(expected.totalPage());

        assertThat(result).usingRecursiveComparison(RecursiveComparisonConfiguration.builder()
            .withIgnoredFields("products.productId")
            .build()).isEqualTo(expected);
    }
}
