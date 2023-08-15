package shop.woowasap.shop.domain.cart;

import lombok.Builder;
import lombok.Getter;
import shop.woowasap.shop.domain.product.Product;

@Getter
public class CartProduct {

    private final Product product;
    private final CartProductQuantity quantity;

    @Builder
    public CartProduct(final Product product, final CartProductQuantity quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}
