package shop.woowasap.shop.domain.api.cart;

import shop.woowasap.shop.domain.api.cart.request.AddCartProductRequest;
import shop.woowasap.shop.domain.api.cart.request.UpdateCartProductRequest;
import shop.woowasap.shop.domain.api.cart.response.CartResponse;

public interface CartUseCase {

    void updateCartProduct(final long userId, final UpdateCartProductRequest updateProductRequest);

    void addCartProduct(final long userId, final AddCartProductRequest addCartProductRequest);

    void deleteCartProduct(final long userId, final long productId);

    CartResponse getCartByUserId(final long userId);
}
