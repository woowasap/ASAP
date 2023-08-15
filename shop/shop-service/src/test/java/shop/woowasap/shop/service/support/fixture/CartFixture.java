package shop.woowasap.shop.service.support.fixture;

import static shop.woowasap.shop.service.support.fixture.ProductFixture.productBuilder;

import java.util.List;
import shop.woowasap.shop.domain.api.cart.response.CartProductResponse;
import shop.woowasap.shop.domain.api.cart.response.CartResponse;
import shop.woowasap.shop.domain.cart.Cart;
import shop.woowasap.shop.domain.cart.CartProduct;
import shop.woowasap.shop.domain.cart.CartProductQuantity;
import shop.woowasap.shop.domain.product.Product;

public final class CartFixture {

    private static final long CART_ID = 1L;
    private static final long PRODUCT_ID = 1L;
    private static final long CART_PRODUCT_QUANTITY = 10;

    private CartFixture() {
    }

    public static CartProduct cartProduct() {
        final Product product = productBuilder(PRODUCT_ID).build();

        return CartProduct.builder()
            .product(product)
            .quantity(new CartProductQuantity(CART_PRODUCT_QUANTITY))
            .build();
    }

    public static Cart Cart(final long userId, final List<CartProduct> cartProducts) {
        return Cart.builder()
            .id(CART_ID)
            .cartProducts(cartProducts)
            .userId(userId)
            .build();
    }

    public static CartResponse cartResponse(final List<CartProduct> cartProducts) {
        final List<CartProductResponse> cartProductResponses = cartProducts.stream()
            .map(CartFixture::cartProductResponse)
            .toList();

        return new CartResponse(CART_ID, cartProductResponses);
    }

    private static CartProductResponse cartProductResponse(final CartProduct cartProduct) {
        return new CartProductResponse(
            cartProduct.getProduct().getId(),
            cartProduct.getProduct().getName().getValue(),
            cartProduct.getProduct().getPrice().getValue().toString(),
            cartProduct.getProduct().getQuantity().getValue()
        );
    }
}
