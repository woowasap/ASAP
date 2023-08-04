package shop.woowasap.accept;

import static org.assertj.core.api.Assertions.assertThat;
import static shop.woowasap.accept.product.ProductFixture.loginRequest;
import static shop.woowasap.accept.product.ProductFixture.registerProductRequest;
import static shop.woowasap.accept.product.ProductFixture.unauthorizedUserLoginRequest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
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
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
    }

    @Test
    @DisplayName("권한이 없는 회원이 /products 요청을 통해서 상품을 생성하는 경우 UNAUTHORIZED 응답을 받는다.")
    void createProductWithUnauthorized() {
        // given
        final LoginRequest unauthorizedUserLoginRequest = unauthorizedUserLoginRequest();
        final String accessToken = AuthApiSupporter.login(unauthorizedUserLoginRequest)
            .as(LoginResponse.class).token();

        // when
        final ExtractableResponse<Response> response = ShopApiSupporter
            .registerProduct(accessToken, registerProductRequest());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_UNAUTHORIZED);
    }
}
