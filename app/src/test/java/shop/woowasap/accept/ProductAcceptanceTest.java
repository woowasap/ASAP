package shop.woowasap.accept;

import static shop.woowasap.accept.support.fixture.ProductFixture.forbiddenUserLoginRequest;
import static shop.woowasap.accept.support.fixture.ProductFixture.loginRequest;
import static shop.woowasap.accept.support.fixture.ProductFixture.registerProductRequest;
import static shop.woowasap.accept.support.valid.HttpValidator.assertForbidden;
import static shop.woowasap.accept.support.valid.ShopValidator.assertProductRegistered;

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
        final String accessToken = "Token";

        // when
        final ExtractableResponse<Response> response = ShopApiSupporter
            .registerProduct(accessToken, registerProductRequest());

        // then
        assertProductRegistered(response);
    }

}
