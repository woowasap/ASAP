package shop.woowasap.shop.domain.in.cart;

import shop.woowasap.shop.domain.in.cart.request.AddCartProductRequest;
import shop.woowasap.shop.domain.in.cart.request.UpdateCartProductRequest;
import shop.woowasap.shop.domain.in.cart.response.CartResponse;

public interface CartUseCase {

    void updateCartProduct(final long userId,
        final UpdateCartProductRequest updateCartProductRequest);

    void addCartProduct(final long userId, final AddCartProductRequest addCartProductRequest);

    void deleteCartProduct(final long userId, final long productId);

    CartResponse getCartByUserId(final long userId);

    void clearCartByUserId(final long userId);
}
