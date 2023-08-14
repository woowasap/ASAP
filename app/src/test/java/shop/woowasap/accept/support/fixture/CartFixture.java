package shop.woowasap.accept.support.fixture;

import java.util.List;
import shop.woowasap.shop.domain.api.cart.response.CartProductResponse;
import shop.woowasap.shop.domain.api.cart.response.CartResponse;

public class CartFixture {

    public static final String NAME = "productName";
    public static final String PRICE = "10000";
    public static final long CART_ID = 1L;

    public static CartResponse cartResponse(final long productId, final long quantity) {
        final CartProductResponse cartProduct = new CartProductResponse(productId, NAME, PRICE,
            quantity);

        return new CartResponse(CART_ID, List.of(cartProduct));
    }
}
