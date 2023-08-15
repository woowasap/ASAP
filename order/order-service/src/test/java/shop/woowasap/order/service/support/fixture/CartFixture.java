package shop.woowasap.order.service.support.fixture;

import java.time.Instant;
import java.util.ArrayList;
import shop.woowasap.shop.domain.cart.Cart;
import shop.woowasap.shop.domain.cart.CartProduct;
import shop.woowasap.shop.domain.cart.CartProductQuantity;
import shop.woowasap.shop.domain.product.Product;

public final class CartFixture {

    private CartFixture() {
    }

    public static Cart.CartBuilder getEmptyCartBuilder() {
        return Cart.builder()
            .id(1L)
            .userId(1L)
            .cartProducts(new ArrayList<>());
    }

    public static CartProduct.CartProductBuilder getCartProductBuilder() {
        return CartProduct.builder()
            .product(ProductFixture.getDefaultBuilder().build())
            .quantity(new CartProductQuantity(10L));
    }
}
