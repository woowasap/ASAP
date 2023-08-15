package shop.woowasap.shop.domain.cart;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public final class Cart {

    private final Long id;
    private final Long userId;
    private final List<CartProduct> cartProducts;

    @Builder
    public Cart(final Long id, final Long userId, final List<CartProduct> cartProducts) {
        this.id = id;
        this.userId = userId;
        this.cartProducts = cartProducts;
    }

    public void addProduct(final CartProduct cartProduct) {
        if (cartProducts.contains(cartProduct)) {
            updateCartProductInCartProducts(cartProduct);
            return;
        }
        cartProducts.add(cartProduct);
    }

    private void updateCartProductInCartProducts(final CartProduct cartProduct) {
        final CartProduct updatedCartProduct = cartProduct.addQuantity(cartProduct.getQuantity());
        cartProducts.remove(cartProduct);
        cartProducts.add(updatedCartProduct);
    }
}
