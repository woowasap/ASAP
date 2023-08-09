package shop.woowasap.accept;

import static shop.woowasap.accept.product.ProductFixture.registerProductRequest;
import static shop.woowasap.accept.product.ProductFixture.updateProductRequest;
import static shop.woowasap.accept.support.api.ShopApiSupporter.registerProduct;
import static shop.woowasap.accept.support.valid.HttpValidator.assertNotFound;
import static shop.woowasap.accept.support.valid.HttpValidator.assertOk;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woowasap.accept.support.api.ShopApiSupporter;
import shop.woowasap.mock.dto.RegisterProductRequest;
import shop.woowasap.shop.service.dto.UpdateProductRequest;

@DisplayName("Product 인수테스트")
class ProductAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("상품 내용을 수정한다.")
    void updateProduct() {
        // given
        final String accessToken = "token";

        final RegisterProductRequest registerProductRequest = registerProductRequest();
        final ExtractableResponse<Response> registerResponse = registerProduct(accessToken,
            registerProductRequest);

        final long productId = Long.parseLong(registerResponse
                            .header("Location")
                            .split("/")[4]);

        final UpdateProductRequest updateProductRequest = updateProductRequest();

        // when
        ExtractableResponse<Response> response = ShopApiSupporter.updateProduct(accessToken,
            productId,
            updateProductRequest);

        // then
        assertOk(response);
    }

    @Test
    @DisplayName("존재하지 않는 상품을 수정하려고 하는경우 NOTFOUND 응답을 받는다.")
    void updateProductWithNotFound() {
        // given
        final String accessToken = "token";

        final long invalidProductId = 123;

        final UpdateProductRequest updateProductRequest = updateProductRequest();

        // when
        ExtractableResponse<Response> response = ShopApiSupporter.updateProduct(accessToken,
            invalidProductId,
            updateProductRequest);

        // then
        assertNotFound(response);
    }

}
