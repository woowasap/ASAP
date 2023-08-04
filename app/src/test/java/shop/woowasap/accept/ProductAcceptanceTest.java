package shop.woowasap.accept;

import static org.assertj.core.api.Assertions.assertThat;
import static shop.woowasap.accept.product.ProductFixture.loginRequest;
import static shop.woowasap.accept.product.ProductFixture.registerProductRequest;
import static shop.woowasap.accept.product.ProductFixture.forbiddenUserLoginRequest;
import static shop.woowasap.accept.support.valid.HttpValidator.assertCreated;
import static shop.woowasap.accept.support.valid.HttpValidator.assertForbidden;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woowasap.accept.support.api.AuthApiSupporter;
import shop.woowasap.accept.support.api.ShopApiSupporter;
import shop.woowasap.mock.dto.LoginRequest;
import shop.woowasap.mock.dto.LoginResponse;

@DisplayName("상품 인수테스트")
class ProductAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("POST /products 요청을 통해서 상품을 생성한다.")
    void createProduct() {
        // given
        final LoginRequest loginRequest = loginRequest();
        final String accessToken = AuthApiSupporter.login(loginRequest)
            .as(LoginResponse.class).token();

        // when
        final ExtractableResponse<Response> response = ShopApiSupporter
            .registerProduct(accessToken, registerProductRequest());

        // then
        assertCreated(response);
        assertThat(response.header("Location")).isNotBlank();
    }

    @Test
    @DisplayName("권한이 없는 회원이 /products 요청을 통해서 상품을 생성하는 경우 FORBIDDEN 응답을 받는다.")
    void createProductWithForbidden() {
        // given
        final LoginRequest forbiddenUserLoginRequest = forbiddenUserLoginRequest();
        final String accessToken = AuthApiSupporter.login(forbiddenUserLoginRequest)
            .as(LoginResponse.class).token();

        // when
        final ExtractableResponse<Response> response = ShopApiSupporter
            .registerProduct(accessToken, registerProductRequest());

        // then
        assertForbidden(response);
    }
}
