package shop.woowasap.accept.support.valid;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import shop.woowasap.mock.dto.ProductsResponse;
import shop.woowasap.mock.dto.ProductsResponse.Product;
import shop.woowasap.shop.service.dto.ProductResponse;

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

    public static void assertProduct(ExtractableResponse<Response> result,
        ProductResponse expected) {
        HttpValidator.assertOk(result);

        assertProductExceptId(result, expected);
    }

    private static void assertProductExceptId(ExtractableResponse<Response> result,
        ProductResponse expected) {
        ProductResponse resultProduct = result.as(ProductResponse.class);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(resultProduct.description()).isEqualTo(expected.description());
            softAssertions.assertThat(resultProduct.name()).isEqualTo(expected.name());
            softAssertions.assertThat(resultProduct.endTime()).isEqualTo(expected.endTime());
            softAssertions.assertThat(resultProduct.startTime()).isEqualTo(expected.startTime());
            softAssertions.assertThat(resultProduct.price()).isEqualTo(expected.price());
            softAssertions.assertThat(resultProduct.quantity()).isEqualTo(expected.quantity());
        });
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
        List<Product> resultList = result.products();
        List<ProductsResponse.Product> expectedList = expected.products();
        assertThat(resultList).hasSize(expectedList.size());

        for (int i = 0; i < resultList.size(); i++) {
            ProductsResponse.Product resultElement = resultList.get(i);
            ProductsResponse.Product expectedElement = expectedList.get(i);

            assertThat(resultElement.name()).isEqualTo(expectedElement.name());
            assertThat(resultElement.price()).isEqualTo(expectedElement.price());
            assertThat(resultElement.endTime()).isEqualTo(expectedElement.endTime());
            assertThat(resultElement.startTime()).isEqualTo(expectedElement.startTime());
        }
    }
}
