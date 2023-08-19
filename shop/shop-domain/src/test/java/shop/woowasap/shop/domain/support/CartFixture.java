package shop.woowasap.shop.domain.support;

import static shop.woowasap.shop.domain.support.ProductFixture.getDefaultBuilder;

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
            .product(getDefaultBuilder().build())
            .quantity(new CartProductQuantity(10L));
    }

    public static CartProduct.CartProductBuilder getCartProductBuilder(final Product product,
        final long quantity) {
        return CartProduct.builder()
            .product(product)
            .quantity(new CartProductQuantity(quantity, product.getQuantity()));
    }
}
