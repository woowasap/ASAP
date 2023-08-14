package shop.woowasap.shop.domain.cart;

import lombok.Builder;
import shop.woowasap.shop.domain.product.Product;

public class CartProduct {

    private final Product product;
    private final Quantity quantity;

    @Builder
    public CartProduct(final Product product, final Quantity quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}
