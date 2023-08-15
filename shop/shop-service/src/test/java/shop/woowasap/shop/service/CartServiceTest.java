package shop.woowasap.shop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static shop.woowasap.shop.service.support.fixture.CartFixture.Cart;
import static shop.woowasap.shop.service.support.fixture.CartFixture.QUANTITY;
import static shop.woowasap.shop.service.support.fixture.CartFixture.addCartProductRequest;
import static shop.woowasap.shop.service.support.fixture.CartFixture.cartProduct;
import static shop.woowasap.shop.service.support.fixture.CartFixture.cartResponse;
import static shop.woowasap.shop.service.support.fixture.CartFixture.emptyCart;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import shop.woowasap.core.id.api.IdGenerator;
import shop.woowasap.shop.domain.api.cart.response.CartResponse;
import shop.woowasap.shop.domain.cart.Cart;
import shop.woowasap.shop.domain.cart.CartProduct;
import shop.woowasap.shop.domain.product.Product;
import shop.woowasap.shop.domain.spi.CartRepository;
import shop.woowasap.shop.domain.spi.ProductRepository;
import shop.woowasap.shop.service.support.fixture.CartFixture;
import shop.woowasap.shop.service.support.fixture.ProductFixture;

@ExtendWith(SpringExtension.class)
@DisplayName("CartService 클래스")
@ContextConfiguration(classes = CartService.class)
class CartServiceTest {

    @Autowired
    private CartService cartService;

    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private IdGenerator idGenerator;

    @Nested
    @DisplayName("Cart Product 를 추가하는 메서드는")
    class AddCartProduct {

        @Test
        @DisplayName("Cart가 존재하지 않는 경우 Cart를 생성하고 해당 CartProduct를 추가한다.")
        void create() {
            // given
            final long cartId = 1L;
            final long userId = 1L;
            final long productId = 1L;
            final Cart cart = CartFixture.cart(cartId);
            final Product product = ProductFixture.validProduct(productId);

            when(cartRepository.existCartByUserId(userId)).thenReturn(false);
            when(idGenerator.generate()).thenReturn(cartId);
            when(cartRepository.getByUserId(userId)).thenReturn(cart);
            when(productRepository.findById(productId)).thenReturn(Optional.of(product));

            // when
            cartService.addCartProduct(userId,
                addCartProductRequest(productId, QUANTITY));

            // then
            verify(cartRepository, times(1)).persist(cart);
        }

        @Test
        @DisplayName("Cart가 존재하는 경우 CartProduct를 추가한다.")
        void createHasCartProduct() {
            // given
            final long cartId = 1L;
            final long userId = 1L;
            final long productId = 1L;
            final Cart cart = CartFixture.cart(cartId);
            final Product product = ProductFixture.validProduct(productId);

            when(cartRepository.existCartByUserId(userId)).thenReturn(true);
            when(idGenerator.generate()).thenReturn(cartId);
            when(cartRepository.getByUserId(userId)).thenReturn(cart);
            when(productRepository.findById(productId)).thenReturn(Optional.of(product));

            // when
            cartService.addCartProduct(userId,
                addCartProductRequest(productId, QUANTITY));

            // then
            verify(cartRepository, times(1)).persist(cart);
        }
    }

    @Nested
    @DisplayName("getCartByUserId 메서드는")
    class GetCartByUserId_Method {

        @Test
        @DisplayName("userId 에 해당하는 user 의 장바구니 상품들을 반환한다")
        void returnCartProducts() {
            // given
            final long userId = 1L;

            final CartProduct cartProduct = cartProduct();
            final CartResponse expected = cartResponse(List.of(cartProduct));

            when(cartRepository.existCartByUserId(userId)).thenReturn(true);
            when(cartRepository.getByUserId(userId)).thenReturn(
                Cart(userId, List.of(cartProduct)));

            // when
            final CartResponse result = cartService.getCartByUserId(userId);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        @DisplayName("userId 에 해당하는 user 의 장바구니가 없다면 장바구니를 생성한다")
        void createEmptyCart_WhenCartNotExist() {
            // given
            final long userId = 1L;

            final CartResponse expected = cartResponse(List.of());
            final Cart emptyCart = emptyCart(userId);

            when(cartRepository.existCartByUserId(userId)).thenReturn(false);
            when(cartRepository.createEmptyCart(eq(userId), anyLong())).thenReturn(emptyCart);
            when(cartRepository.getByUserId(userId)).thenReturn(emptyCart);

            // when
            final CartResponse result = cartService.getCartByUserId(userId);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
            verify(cartRepository).persist(emptyCart);
        }
    }

}
