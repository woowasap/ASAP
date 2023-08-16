package shop.woowasap.accept.support.fixture;

import java.util.List;
import shop.woowasap.shop.domain.api.cart.request.AddCartProductRequest;
import shop.woowasap.shop.domain.api.cart.request.UpdateCartProductRequest;
import shop.woowasap.shop.domain.api.cart.response.CartProductResponse;
import shop.woowasap.shop.domain.api.cart.response.CartResponse;

public final class CartFixture {

    public static final String NAME = "productName";
    public static final String PRICE = "10000";
    public static final long CART_ID = 1L;

    private CartFixture() {
    }

    public static AddCartProductRequest addCartProductRequest(final long productId,
        final long quantity) {
        return new AddCartProductRequest(productId, quantity);
    }

    public static UpdateCartProductRequest updateCartProductRequest(final long productId,
        final long quantity) {
        return new UpdateCartProductRequest(productId, quantity);
    }

    public static CartResponse cartResponse(final long productId, final long quantity) {
        final CartProductResponse cartProduct = new CartProductResponse(productId, NAME, PRICE,
            quantity);

        return new CartResponse(CART_ID, List.of(cartProduct));
    }
}
