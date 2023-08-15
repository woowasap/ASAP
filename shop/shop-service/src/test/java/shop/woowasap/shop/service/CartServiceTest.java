package shop.woowasap.shop.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static shop.woowasap.shop.service.support.fixture.CartFixture.QUANTITY;
import static shop.woowasap.shop.service.support.fixture.CartFixture.addCartProductRequest;

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
import shop.woowasap.shop.domain.cart.Cart;
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
}
