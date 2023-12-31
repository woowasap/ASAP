package shop.woowasap.shop.domain.cart;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import shop.woowasap.shop.domain.product.Product;

@Getter
@EqualsAndHashCode
public class CartProduct {

    @EqualsAndHashCode.Include
    private final Product product;

    @EqualsAndHashCode.Exclude
    private final CartProductQuantity quantity;

    @Builder
    public CartProduct(final Product product, final CartProductQuantity quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public CartProduct addQuantity(final CartProductQuantity quantity) {
        return CartProduct.builder()
            .product(this.product)
            .quantity(this.quantity.addQuantity(quantity, this.product.getQuantity()))
            .build();
    }

    public boolean isSameProduct(final Product product) {
        return this.product.equals(product);
    }

    public CartProduct updateQuantity(final CartProductQuantity quantity) {
        return CartProduct.builder()
            .product(this.product)
            .quantity(quantity)
            .build();
    }
}
