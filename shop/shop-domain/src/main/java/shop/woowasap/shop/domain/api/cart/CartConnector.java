package shop.woowasap.shop.domain.api.cart;

import java.util.Optional;
import shop.woowasap.shop.domain.cart.Cart;

public interface CartConnector {

    Optional<Cart> findByCartIdAndUserId(final long cartId, final long userId);

}
