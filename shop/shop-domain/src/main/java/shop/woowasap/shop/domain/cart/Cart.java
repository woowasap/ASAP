package shop.woowasap.shop.domain.cart;

import java.util.List;
import lombok.Builder;

public class Cart {

    private final Long id;
    private final Long userId;
    private final List<CartProduct> cartProducts;

    @Builder
    public Cart(Long id, Long userId, List<CartProduct> cartProducts) {
        this.id = id;
        this.userId = userId;
        this.cartProducts = cartProducts;
    }
}
