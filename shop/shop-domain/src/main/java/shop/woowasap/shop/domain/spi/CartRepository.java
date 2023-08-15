package shop.woowasap.shop.domain.spi;

import shop.woowasap.shop.domain.cart.Cart;

public interface CartRepository {

    Cart createEmptyCart(final Long cartId, final Long userId);

    Cart persist(final Cart cart);

    boolean existCartByUserId(final long userId);

    Cart getByUserId(final long userId);
}
