package shop.woowasap.shop.service.support.fixture;

import java.util.ArrayList;
import shop.woowasap.shop.domain.api.cart.request.AddCartProductRequest;
import shop.woowasap.shop.domain.cart.Cart;
import shop.woowasap.shop.domain.cart.CartProduct;
import shop.woowasap.shop.domain.cart.CartProductQuantity;
import shop.woowasap.shop.domain.product.Product;

public final class CartFixture {

    public final static Long USER_ID = 1L;
    public final static Long QUANTITY = 10L;

    private CartFixture() {
    }

    public static Cart.CartBuilder getEmptyCartBuilder() {
        return Cart.builder()
            .id(1L)
            .userId(1L)
            .cartProducts(new ArrayList<>());
    }

    public static CartProduct.CartProductBuilder getCartProductBuilder(final Product product) {
        return CartProduct.builder()
            .product(product)
            .quantity(new CartProductQuantity(QUANTITY));
    }

    public static Cart cart(final Long id) {
        return Cart.builder()
            .id(id)
            .userId(USER_ID)
            .cartProducts(new ArrayList<>())
            .build();
    }

    public static AddCartProductRequest addCartProductRequest(final Long productId, final Long quantity) {
        return new AddCartProductRequest(productId, quantity);
    }
}
