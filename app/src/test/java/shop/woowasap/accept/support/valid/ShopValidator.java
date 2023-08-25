package shop.woowasap.accept.support.valid;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import shop.woowasap.shop.domain.in.product.response.ProductDetailsResponse;
import shop.woowasap.shop.domain.in.product.response.ProductsAdminResponse;
import shop.woowasap.shop.domain.in.product.response.ProductsResponse;

public final class ShopValidator {

    private static final RecursiveComparisonConfiguration IGNORE_ID_COMPARISON = RecursiveComparisonConfiguration.builder()
        .withIgnoredFields("id", "startTime", "endTime")
        .build();

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
        assertThat(result).usingRecursiveComparison(RecursiveComparisonConfiguration.builder()
            .withIgnoredFields("products.productId", "products.startTime", "products.endTime")
            .build()).isEqualTo(expected);
    }

    public static void assertAdminProductsFound(ExtractableResponse<Response> result,
        ProductsAdminResponse expected) {
        HttpValidator.assertOk(result);

        ProductsAdminResponse resultResponse = result.as(ProductsAdminResponse.class);
        assertAdminProducts(resultResponse, expected);
    }

    private static void assertAdminProducts(ProductsAdminResponse result, ProductsAdminResponse expected) {
        assertThat(result.page()).isEqualTo(expected.page());
        assertThat(result.totalPage()).isEqualTo(expected.totalPage());

        assertThat(result).usingRecursiveComparison(RecursiveComparisonConfiguration.builder()
            .withIgnoredFields("products.productId", "products.startTime", "products.endTime")
            .build()).isEqualTo(expected);
    }

    public static void assertProduct(ExtractableResponse<Response> result,
        ProductDetailsResponse expected) {
        HttpValidator.assertOk(result);

        ProductDetailsResponse productResponse = result.as(ProductDetailsResponse.class);

        assertThat(productResponse).usingRecursiveComparison(IGNORE_ID_COMPARISON)
            .isEqualTo(expected);
    }
}
