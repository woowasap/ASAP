package shop.woowasap.accept;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woowasap.accept.support.api.ShopApiSupporter;
import shop.woowasap.accept.support.fixture.ProductDtoFixture;
import shop.woowasap.accept.support.valid.HttpValidator;
import shop.woowasap.accept.support.valid.ShopValidator;
import shop.woowasap.mock.dto.ProductResponse;
import shop.woowasap.mock.dto.ProductsResponse;
import shop.woowasap.mock.dto.RegisterProductRequest;

@DisplayName("Product 인수테스트")
class ProductAcceptanceTest {

    @Test
    @DisplayName("저장되어있는 특정 상품을 조회할경우, 상품의 정보가 응답된다.")
    void findSpecificProduct() {
        // given
        String token = "MOCK TOKEN";
        RegisterProductRequest registerProductRequest = ProductDtoFixture.registerProductRequest();

        ShopApiSupporter.registerProduct(token, registerProductRequest);
        ProductsResponse productsResponse = ShopApiSupporter.getAllProducts()
            .as(ProductsResponse.class);

        long anyProductId = productsResponse.products().get(0).productId();
        ProductResponse expected = ProductDtoFixture.productResponse(registerProductRequest);

        // when
        ExtractableResponse<Response> result = ShopApiSupporter.getProduct(anyProductId);

        // then
        ShopValidator.assertProduct(result, expected);
    }

    @Test
    @DisplayName("productId에 해당하는 상품을 찾을 수 없다면, 400 BadRequest가 응답된다.")
    void returnBadRequestWhenCannotFoundProduct() {
        // given
        long notFoundProductId = 0;

        // when
        ExtractableResponse<Response> result = ShopApiSupporter.getProduct(notFoundProductId);

        // then
        HttpValidator.assertBadRequest(result);
    }

}
