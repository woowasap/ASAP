package shop.woowasap.accept;

import static shop.woowasap.accept.support.api.CartApiSupporter.getCartProducts;
import static shop.woowasap.accept.support.api.ShopApiSupporter.registerProduct;
import static shop.woowasap.accept.support.fixture.CartFixture.cartResponse;
import static shop.woowasap.accept.support.fixture.ProductFixture.registerProductRequest;
import static shop.woowasap.accept.support.valid.CartValidator.assertCartProductsFound;
import static shop.woowasap.accept.support.valid.HttpValidator.assertBadRequest;
import static shop.woowasap.accept.support.valid.HttpValidator.assertOk;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woowasap.accept.support.api.AuthApiSupporter;
import shop.woowasap.accept.support.api.CartApiSupporter;
import shop.woowasap.accept.support.fixture.CartFixture;
import shop.woowasap.accept.support.fixture.ProductFixture;
import shop.woowasap.shop.domain.in.cart.request.AddCartProductRequest;
import shop.woowasap.shop.domain.in.cart.request.UpdateCartProductRequest;
import shop.woowasap.shop.domain.in.cart.response.CartResponse;
import shop.woowasap.shop.domain.in.product.request.RegisterProductRequest;

class CartAcceptanceTest extends AcceptanceTest {

    private String accessToken;

    @BeforeEach
    void setAccessToken() {
        accessToken = AuthApiSupporter.adminAccessToken();
    }

    @Test
    @DisplayName("POST /v1/carts 요청을 통해서 해당 상품을 장바구니에 추가한다.")
    void addCartProduct() {
        // given
        final long quantity = 10L;
        final long productId = Long.parseLong(registerProduct(accessToken,
            ProductFixture.registerProductRequest())
            .header("Location")
            .split("/")[4]);

        // when
        final ExtractableResponse<Response> response = CartApiSupporter
            .addCartProduct(accessToken, CartFixture.addCartProductRequest(productId, quantity));

        // then
        assertOk(response);
    }

    @Test
    @DisplayName("POST /v1/carts 요청을 통해서 이미 담겨져 있는 상품을 추가로 상품을 장바구니에 추가하는 경우 개수가 증가한다.")
    void addCartProductAlreadyInCart() {
        // given
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
        assertOk(response);
    }

    @Test
    @DisplayName("장바구니에 있는 상품을 조회한다.")
    void findCartProducts() {
        // given
        final long quantity = 10L;

        final RegisterProductRequest registerProductRequest = registerProductRequest();
        final ExtractableResponse<Response> registerResponse = registerProduct(accessToken,
            registerProductRequest);
        final long productId = Long.parseLong(registerResponse.header("Location").split("/")[4]);

        final AddCartProductRequest addCartProductRequest = new AddCartProductRequest(productId,
            quantity);
        CartApiSupporter.addCartProduct(accessToken, addCartProductRequest);

        final CartResponse expected = cartResponse(productId, quantity,
            registerProductRequest.quantity());

        // when
        final ExtractableResponse<Response> response = getCartProducts(accessToken);

        // then
        assertCartProductsFound(response, expected);
    }

    @Test
    @DisplayName("장바구니에 있는 상품의 수량을 수정한다.")
    void updateCartProduct() {
        // given
        final long updatedQuantity = 10L;
        final ExtractableResponse<Response> registerResponse = registerProduct(accessToken,
            registerProductRequest());

        final long productId = Long.parseLong(registerResponse.header("Location").split("/")[4]);
        final UpdateCartProductRequest updateCartProductRequest = new UpdateCartProductRequest(
            productId, updatedQuantity);

        final AddCartProductRequest addCartProductRequest = new AddCartProductRequest(productId,
            5L);
        CartApiSupporter.addCartProduct(accessToken, addCartProductRequest);

        final CartResponse expected = cartResponse(productId, updatedQuantity,
            updateCartProductRequest.quantity());

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

    @Test
    @DisplayName("장바구니에 있는 상품을 제거한다.")
    void deleteCartProducts() {
        // given
        final RegisterProductRequest registerProductRequest = registerProductRequest();
        final ExtractableResponse<Response> registerResponse = registerProduct(accessToken,
            registerProductRequest);
        final long productId = Long.parseLong(registerResponse.header("Location").split("/")[4]);
        CartApiSupporter
            .addCartProduct(accessToken, CartFixture.addCartProductRequest(productId, 10L));

        // when
        final ExtractableResponse<Response> response = CartApiSupporter.deleteCartProduct(
            accessToken, productId);

        // then
        assertOk(response);
    }

    @Test
    @DisplayName("ProductId에 해당하는 상품이 장바구니에 존재하지 않는다면, 400 BadRequest 를 응답한다.")
    void returnBadRequestWhenCannotFoundProductInCart() {
        // given
        final long notExitProductId = 517;

        // when
        final ExtractableResponse<Response> response = CartApiSupporter.deleteCartProduct(
            accessToken, notExitProductId);

        // then
        assertBadRequest(response);
    }

}
