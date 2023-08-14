package shop.woowasap.shop.domain.api.cart;

import shop.woowasap.shop.domain.api.cart.request.AddCartProductRequest;
import shop.woowasap.shop.domain.api.cart.response.CartResponse;
import shop.woowasap.shop.domain.api.product.request.UpdateProductRequest;

public interface CartUseCase {

    void updateCartProduct(final long userId, final UpdateProductRequest updateProductRequest);

    void addCartProduct(final long userId, final AddCartProductRequest addCartProductRequest);

    void deleteCartProduct(final long userId, final long productId);

    CartResponse getCartByUserId(final long userId);
}
