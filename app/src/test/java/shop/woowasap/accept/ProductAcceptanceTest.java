package shop.woowasap.accept;

import static shop.woowasap.accept.product.ProductFixture.forbiddenUserLoginRequest;
import static shop.woowasap.accept.product.ProductFixture.loginRequest;
import static shop.woowasap.accept.product.ProductFixture.updateProductRequest;
import static shop.woowasap.accept.support.valid.HttpValidator.assertForbidden;
import static shop.woowasap.accept.support.valid.HttpValidator.assertOk;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woowasap.accept.support.api.AuthApiSupporter;
import shop.woowasap.accept.support.api.ShopApiSupporter;
import shop.woowasap.mock.dto.LoginRequest;
import shop.woowasap.mock.dto.LoginResponse;
import shop.woowasap.mock.dto.UpdateProductRequest;

@DisplayName("Product 인수테스트")
class ProductAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("상품 내용을 수정한다.")
    void updateProduct() {
        // given
        final LoginRequest loginRequest = loginRequest();
        final String accessToken = AuthApiSupporter.login(loginRequest)
            .as(LoginResponse.class).token();
        final UpdateProductRequest updateProductRequest = updateProductRequest();

        // when
        ExtractableResponse<Response> response = ShopApiSupporter.updateProduct(accessToken,
            updateProductRequest);

        // then
        assertOk(response);
    }

    @Test
    @DisplayName("권한이 없는 회원이 상품을 수정하는 경우 FORBIDDEN 응답을 받는다.")
    void updateProductWithForbidden() {
        // given
        final LoginRequest forbiddenUserLoginRequest = forbiddenUserLoginRequest();
        final String accessToken = AuthApiSupporter.login(forbiddenUserLoginRequest)
            .as(LoginResponse.class).token();
        final UpdateProductRequest updateProductRequest = updateProductRequest();

        // when
        ExtractableResponse<Response> response = ShopApiSupporter.updateProduct(accessToken,
            updateProductRequest);

        // then
        assertForbidden(response);
    }

}
