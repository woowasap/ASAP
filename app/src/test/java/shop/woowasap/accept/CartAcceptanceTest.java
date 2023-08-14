package shop.woowasap.accept;

import static shop.woowasap.accept.support.api.CartApiSupporter.addCartProduct;
import static shop.woowasap.accept.support.api.CartApiSupporter.getCartProducts;
import static shop.woowasap.accept.support.api.ShopApiSupporter.registerProduct;
import static shop.woowasap.accept.support.fixture.CartFixture.cartResponse;
import static shop.woowasap.accept.support.fixture.ProductFixture.registerProductRequest;
import static shop.woowasap.accept.support.valid.CartValidator.assertCartFound;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woowasap.shop.domain.api.cart.request.AddCartProductRequest;
import shop.woowasap.shop.domain.api.cart.response.CartResponse;
import shop.woowasap.shop.domain.api.product.request.RegisterProductRequest;

@DisplayName("Cart 인수테스트")
class CartAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("장바구니에 있는 상품을 조회한다.")
    void findCartProducts() {
        // given
        final String accessToken = "Token";
        final long quantity = 10L;

        final RegisterProductRequest registerProductRequest = registerProductRequest();
        final ExtractableResponse<Response> registerResponse = registerProduct(accessToken,
            registerProductRequest);
        final long productId = Long.parseLong(registerResponse.header("Location").split("/")[4]);

        final AddCartProductRequest addCartProductRequest = new AddCartProductRequest(productId,
            quantity);
        addCartProduct(accessToken, addCartProductRequest);

        final CartResponse expected = cartResponse(productId, quantity);

        // when
        final ExtractableResponse<Response> response = getCartProducts(accessToken);

        // then
        assertCartFound(response, expected);
    }

}
