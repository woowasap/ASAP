package shop.woowasap.accept.support.valid;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
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

        assertProductsExceptId(result, expected);
    }

    private static void assertProductsExceptId(ProductsResponse result, ProductsResponse expected) {
        List<ProductsResponse.ProductResponse> resultList = result.products();
        List<ProductsResponse.ProductResponse> expectedList = expected.products();
        assertThat(resultList).hasSize(expectedList.size());

        for (int i = 0; i < resultList.size(); i++) {
            ProductsResponse.ProductResponse resultElement = resultList.get(i);
            ProductsResponse.ProductResponse expectedElement = expectedList.get(i);

            assertThat(resultElement.name()).isEqualTo(expectedElement.name());
            assertThat(resultElement.price()).isEqualTo(expectedElement.price());
            assertThat(resultElement.endTime()).isEqualTo(expectedElement.endTime());
            assertThat(resultElement.startTime()).isEqualTo(expectedElement.startTime());
        }
    }
}
