package shop.woowasap.shop.service.mapper;

import java.util.List;
import shop.woowasap.shop.domain.in.cart.response.CartProductResponse;
import shop.woowasap.shop.domain.in.cart.response.CartResponse;
import shop.woowasap.shop.domain.cart.Cart;


public final class CartMapper {

    private CartMapper() {
    }

    public static CartResponse toCartResponse(final Cart cart) {
        final List<CartProductResponse> cartProducts = cart.getCartProducts()
            .stream()
            .map(cartProduct -> new CartProductResponse(
                cartProduct.getProduct().getId(),
                cartProduct.getProduct().getName().getValue(),
                cartProduct.getProduct().getPrice().getValue().toString(),
                cartProduct.getQuantity().getValue(),
                cartProduct.getProduct().getQuantity().getValue()
            )).toList();

        return new CartResponse(cart.getId(), cartProducts);
    }
}
