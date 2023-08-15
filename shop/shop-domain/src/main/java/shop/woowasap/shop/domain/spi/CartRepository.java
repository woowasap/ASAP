package shop.woowasap.shop.domain.spi;

import shop.woowasap.shop.domain.cart.Cart;

public interface CartRepository {

    Cart createEmptyCart(final long userId, final long cartId);

    Cart persist(final Cart cart);

    boolean existCartByUserId(final long userId);

    Cart getByUserId(final long userId);
}
