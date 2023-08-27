package shop.woowasap.shop.domain.in.cart;

import java.util.Optional;
import shop.woowasap.shop.domain.cart.Cart;

public interface CartConnector {

    Optional<Cart> findByCartIdAndUserId(final long cartId, final long userId);

    void clearCartByUserId(final long userId);
}
