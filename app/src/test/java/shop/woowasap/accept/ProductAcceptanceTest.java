package shop.woowasap.accept;

import static shop.woowasap.accept.product.ProductFixture.registerProductRequest;
import static shop.woowasap.accept.product.ProductFixture.updateProductRequest;
import static shop.woowasap.accept.support.api.ShopApiSupporter.registerProduct;
import static shop.woowasap.accept.support.valid.HttpValidator.assertBadRequest;
import static shop.woowasap.accept.support.valid.HttpValidator.assertOk;
import static shop.woowasap.accept.support.valid.ShopValidator.assertProductRegistered;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woowasap.accept.support.api.ShopApiSupporter;
import shop.woowasap.accept.support.fixture.ProductFixture;
import shop.woowasap.accept.support.valid.HttpValidator;
import shop.woowasap.accept.support.valid.ShopValidator;
import shop.woowasap.mock.dto.ProductsResponse;
import shop.woowasap.shop.app.api.request.RegisterProductRequest;
import shop.woowasap.shop.app.api.request.UpdateProductRequest;
import shop.woowasap.shop.app.api.response.ProductResponse;

@DisplayName("Product 인수테스트")
class ProductAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("상품 내용을 수정한다.")
    void updateProduct() {
        // given
        final String accessToken = "Token";

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
    @DisplayName("존재하지 않는 상품을 수정하려고 하는경우 BADREQUEST 응답을 받는다.")
    void updateProductWithNotFound() {
        // given
        final String accessToken = "Token";

        final long invalidProductId = 123;

        final UpdateProductRequest updateProductRequest = updateProductRequest();

        // when
        ExtractableResponse<Response> response = ShopApiSupporter.updateProduct(accessToken,
            invalidProductId,
            updateProductRequest);

        // then
        assertBadRequest(response);
    }

    @Test
    @DisplayName("POST /products 요청을 통해서 상품을 생성한다.")
    void createProduct() {
        // given
        final String accessToken = "Token";

        // when
        final ExtractableResponse<Response> response = ShopApiSupporter
            .registerProduct(accessToken, ProductFixture.registerProductRequest());

        // then
        assertProductRegistered(response);
    }

    @Test
    @DisplayName("저장되어있는 특정 상품을 조회할경우, 상품의 정보가 응답된다.")
    void findSpecificProduct() {
        // given
        final String token = "MOCK TOKEN";
        final RegisterProductRequest registerProductRequest = ProductFixture.registerProductRequest();

        ShopApiSupporter.registerProduct(token, registerProductRequest);
        final ProductsResponse productsResponse = ShopApiSupporter.getAllProducts()
            .as(ProductsResponse.class);

        final long anyProductId = productsResponse.products().get(0).productId();
        ProductResponse expected = ProductFixture.productResponse(registerProductRequest);

        // when
        ExtractableResponse<Response> result = ShopApiSupporter.getProduct(anyProductId);

        // then
        ShopValidator.assertProduct(result, expected);
    }

    @Test
    @DisplayName("productId에 해당하는 상품을 찾을 수 없다면, 400 BadRequest가 응답된다.")
    void returnBadRequestWhenCannotFoundProduct() {
        // given
        final long notFoundProductId = 123L;

        // when
        ExtractableResponse<Response> result = ShopApiSupporter.getProduct(notFoundProductId);

        // then
        HttpValidator.assertBadRequest(result);
    }
}
