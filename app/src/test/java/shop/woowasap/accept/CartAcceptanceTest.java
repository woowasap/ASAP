package shop.woowasap.accept;

import static shop.woowasap.accept.support.api.CartApiSupporter.getCartProducts;
import static shop.woowasap.accept.support.api.ShopApiSupporter.registerProduct;
import static shop.woowasap.accept.support.fixture.CartFixture.cartResponse;
import static shop.woowasap.accept.support.fixture.ProductFixture.registerProductRequest;
import static shop.woowasap.accept.support.valid.CartValidator.assertCartProductsFound;
import static shop.woowasap.accept.support.valid.HttpValidator.assertBadRequest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woowasap.accept.support.api.CartApiSupporter;
import shop.woowasap.accept.support.fixture.CartFixture;
import shop.woowasap.accept.support.fixture.ProductFixture;
import shop.woowasap.accept.support.valid.HttpValidator;
import shop.woowasap.shop.domain.api.cart.request.AddCartProductRequest;
import shop.woowasap.shop.domain.api.cart.request.UpdateCartProductRequest;
import shop.woowasap.shop.domain.api.cart.response.CartResponse;
import shop.woowasap.shop.domain.api.product.request.RegisterProductRequest;

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

        // when
        final ExtractableResponse<Response> response = CartApiSupporter
            .addCartProduct(accessToken, CartFixture.addCartProductRequest(productId, quantity));

        // then
        HttpValidator.assertOk(response);
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

        // when
        CartApiSupporter
            .addCartProduct(accessToken, CartFixture.addCartProductRequest(productId, quantity));
        final ExtractableResponse<Response> response = CartApiSupporter
            .addCartProduct(accessToken, CartFixture.addCartProductRequest(productId, addQuantity));

        // then
        HttpValidator.assertOk(response);
    }

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
        CartApiSupporter.addCartProduct(accessToken, addCartProductRequest);

        final CartResponse expected = cartResponse(productId, quantity);

        // when
        final ExtractableResponse<Response> response = getCartProducts(accessToken);

        // then
        assertCartProductsFound(response, expected);
    }

    @Test
    @DisplayName("장바구니에 있는 상품의 수량을 수정한다.")
    void updateCartProduct() {
        // given
        final String accessToken = "Token";
        final long updatedQuantity = 10L;
        final ExtractableResponse<Response> registerResponse = registerProduct(accessToken,
            registerProductRequest());

        final long productId = Long.parseLong(registerResponse.header("Location").split("/")[4]);
        final UpdateCartProductRequest updateCartProductRequest = new UpdateCartProductRequest(
            productId, updatedQuantity);

        final AddCartProductRequest addCartProductRequest = new AddCartProductRequest(productId,
            5L);
        CartApiSupporter.addCartProduct(accessToken, addCartProductRequest);

        final CartResponse expected = cartResponse(productId, updatedQuantity);

        // when
        CartApiSupporter.updateCartProduct(accessToken, updateCartProductRequest);

        // then
        final ExtractableResponse<Response> cartProducts = getCartProducts(accessToken);
        assertCartProductsFound(cartProducts, expected);
    }

    @Test
    @DisplayName("해당 장바구니에 담겨져 있지 않은 상품의 수량을 수정하는 경우 400 BadRequest를 반환한다.")
    void updateNotExistsCartProduct() {
        // given
        final String accessToken = "Token";
        final long invalidProductId = 999L;
        final long quantity = 10L;
        final UpdateCartProductRequest updateCartProductRequest = new UpdateCartProductRequest(
            invalidProductId, quantity);

        // when
        final ExtractableResponse<Response> response = CartApiSupporter.updateCartProduct(
            accessToken, updateCartProductRequest);

        // then
        assertBadRequest(response);
    }

}
