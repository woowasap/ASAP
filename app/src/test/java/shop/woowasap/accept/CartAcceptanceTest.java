package shop.woowasap.accept;

import static shop.woowasap.accept.support.api.ShopApiSupporter.registerProduct;
import static shop.woowasap.accept.support.fixture.CartFixture.cartResponse;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woowasap.accept.support.api.CartApiSupporter;
import shop.woowasap.accept.support.fixture.CartFixture;
import shop.woowasap.accept.support.fixture.ProductFixture;
import shop.woowasap.accept.support.valid.CartValidator;
import shop.woowasap.accept.support.valid.HttpValidator;
import shop.woowasap.shop.domain.api.cart.response.CartResponse;

class CartAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("POST /v1/carts 요청을 통해서 해당 상품을 장바구니에 추가한다.")
    void addCartProduct() {
        // given
        final String accessToken = "Token";
        final long quantity = 10L;
        final long productId = Long.parseLong(registerProduct(accessToken,
            ProductFixture.registerProductRequest())
            .header("Location")
            .split("/")[4]);
        final CartResponse cartResponse = cartResponse(productId, quantity);

        // when
        final ExtractableResponse<Response> response = CartApiSupporter
            .addCartProduct(accessToken, CartFixture.addCartProductRequest(productId, quantity));

        // then
        CartValidator.assertCartFound(response, cartResponse);
    }

    @Test
    @DisplayName("POST /v1/carts 요청을 통해서 이미 담겨져 있는 상품을 추가로 상품을 장바구니에 추가하는 경우 개수가 증가한다.")
    void addCartProductAlreadyInCart() {
        // given
        final String accessToken = "Token";
        final long quantity = 10L;
        final long addQuantity = 5L;
        final long productId = Long.parseLong(registerProduct(accessToken,
            ProductFixture.registerProductRequest())
            .header("Location")
            .split("/")[4]);
        final CartResponse cartResponse = cartResponse(productId, quantity + addQuantity);

        // when
        CartApiSupporter
            .addCartProduct(accessToken, CartFixture.addCartProductRequest(productId, quantity));
        final ExtractableResponse<Response> response = CartApiSupporter
            .addCartProduct(accessToken, CartFixture.addCartProductRequest(productId, addQuantity));

        // then
        CartValidator.assertCartFound(response, cartResponse);
    }
}
