package shop.woowasap.shop.repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import shop.woowasap.shop.domain.cart.Cart;
import shop.woowasap.shop.domain.cart.CartProduct;
import shop.woowasap.shop.domain.cart.CartProductQuantity;
import shop.woowasap.shop.domain.product.Product;
import shop.woowasap.shop.domain.spi.CartRepository;

@Repository
public class MockCartRepository implements CartRepository {

    public static final String NAME = "productName";
    public static final String DESCRIPTION = "productDescription";
    public static final String PRICE = "10000";

    public static final long PRODUCT_QUANTITY = 10L;
    public static final long ORDER_QUANTITY = 10L;

    public static final long MOCK_CART_ID = 1L;
    public static final long MOCK_PRODUCT_ID = 1L;

    private static final String OFFSET_ID = "+09:00";

    public static final LocalDateTime START_TIME = LocalDateTime.of(2023, 8, 5, 12, 30);
    public static final LocalDateTime END_TIME = LocalDateTime.of(2023, 8, 5, 14, 30);

    @Override
    public Cart createEmptyCart(final Cart cart) {
        return null;
    }

    @Override
    public Cart createEmptyCart(final long userId, final long cartId) {
        return new Cart(MOCK_CART_ID, userId, new ArrayList<>());
    }

    @Override
    public Cart persist(final Cart cart) {
        return cart;
    }

    @Override
    public boolean existCartByUserId(final long userId) {
        return true;
    }

    @Override
    public Cart getByUserId(final long userId) {
        final Product product = Product.builder()
            .id(MOCK_PRODUCT_ID)
            .name(NAME)
            .description(DESCRIPTION)
            .price(PRICE)
            .quantity(PRODUCT_QUANTITY)
            .startTime(START_TIME.toInstant(ZoneOffset.of(OFFSET_ID)))
            .endTime(END_TIME.toInstant(ZoneOffset.of(OFFSET_ID))).build();

        final List<CartProduct> cartProducts = new ArrayList<>();
        cartProducts.add(CartProduct.builder()
            .product(product)
            .quantity(new CartProductQuantity(ORDER_QUANTITY))
            .build());

        return Cart.builder()
            .id(MOCK_CART_ID)
            .userId(userId)
            .cartProducts(cartProducts)
            .build();
    }
}
