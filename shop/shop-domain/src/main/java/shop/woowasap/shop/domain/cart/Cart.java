package shop.woowasap.shop.domain.cart;

import java.text.MessageFormat;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import shop.woowasap.shop.domain.exception.CannotFindProductInCartException;
import shop.woowasap.shop.domain.product.Product;

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

    public void deleteProduct(final Product product) {
        validateProduct(product);
        cartProducts.removeIf(cartProduct -> cartProduct.isSameProduct(product));
    }

    private void validateProduct(final Product product) {
        if (cartProducts.stream().noneMatch(cartProduct -> cartProduct.isSameProduct(product))) {
            throw new CannotFindProductInCartException(
                MessageFormat.format(
                    "product 가 장바구니에 존재하지 않습니다. productId : \"{0}\"",
                    product.getId())
            );
        }
    }
}

