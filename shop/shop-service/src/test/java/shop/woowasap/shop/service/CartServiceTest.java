package shop.woowasap.shop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
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
import static shop.woowasap.shop.service.support.fixture.CartFixture.updateCartProductRequest;
import static shop.woowasap.shop.service.support.fixture.ProductFixture.onSaleProduct;
import static shop.woowasap.shop.service.support.fixture.ProductFixture.onSaleProductBuilder;

import java.util.ArrayList;
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
import shop.woowasap.shop.domain.cart.Cart;
import shop.woowasap.shop.domain.cart.CartProduct;
import shop.woowasap.shop.domain.exception.NotExistsCartProductException;
import shop.woowasap.shop.domain.exception.NotExistsProductException;
import shop.woowasap.shop.domain.in.cart.response.CartResponse;
import shop.woowasap.shop.domain.out.CartRepository;
import shop.woowasap.shop.domain.out.ProductRepository;
import shop.woowasap.shop.domain.product.Product;
import shop.woowasap.shop.service.support.fixture.CartFixture;

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
            final Product product = onSaleProduct(productId);

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
            final Product product = onSaleProduct(productId);

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
        }
    }

    @Nested
    @DisplayName("Cart Product 를 수정하는 메서드는")
    class UpdateCartProduct {

        @Test
        @DisplayName("장바구니에 해당 상품이 있다면 개수를 정상적으로 변경한다.")
        void update() {
            // given
            final long cartId = 1L;
            final long userId = 1L;
            final long productId = 1L;
            final long updateQuantity = 20L;
            final Product product = onSaleProduct(productId);
            final List<CartProduct> cartProducts = new ArrayList<>();
            cartProducts.add(CartFixture.getCartProductBuilder(product).build());
            final Cart cart = CartFixture.getEmptyCartBuilder().cartProducts(cartProducts)
                .build();

            when(cartRepository.existCartByUserId(userId)).thenReturn(true);
            when(idGenerator.generate()).thenReturn(cartId);
            when(cartRepository.getByUserId(userId)).thenReturn(cart);
            when(productRepository.findById(productId)).thenReturn(Optional.of(product));

            // when
            cartService.updateCartProduct(userId,
                updateCartProductRequest(productId, updateQuantity));

            // then
            verify(cartRepository, times(1)).persist(cart);
        }

        @Test
        @DisplayName("장바구니에 해당 상품이 없다면 예외를 던진다.")
        void updateNotExistsCartProductThrowException() {
            // given
            final long cartId = 1L;
            final long userId = 1L;
            final long productId = 1L;
            final long updateQuantity = 20L;
            final Cart cart = CartFixture.cart(cartId);
            final Product product = onSaleProduct(productId);

            when(cartRepository.existCartByUserId(userId)).thenReturn(false);
            when(idGenerator.generate()).thenReturn(cartId);
            when(cartRepository.getByUserId(userId)).thenReturn(cart);
            when(productRepository.findById(productId)).thenReturn(Optional.of(product));

            // when
            Exception exception = catchException(() -> cartService.updateCartProduct(userId,
                updateCartProductRequest(productId, updateQuantity)));

            // then
            assertThat(exception).isInstanceOf(NotExistsCartProductException.class);
        }
    }

    @Nested
    @DisplayName("deleteCartProduct 메서드는")
    class DeleteCartProduct_Method {

        @Test
        @DisplayName("cart 에서 product 를 제거한다.")
        void deleteProductInCart() {
            // given
            final long cartId = 1L;
            final long userId = 1L;
            final long deleteProductId = 1L;

            final Product deleteProduct = onSaleProductBuilder(deleteProductId)
                .build();
            final CartProduct deleteCartProduct = CartFixture.getCartProductBuilder(deleteProduct)
                .build();
            final Cart cart = Cart(userId, new ArrayList<>(List.of(deleteCartProduct)));

            when(cartRepository.existCartByUserId(userId)).thenReturn(true);
            when(idGenerator.generate()).thenReturn(cartId);
            when(cartRepository.getByUserId(userId)).thenReturn(cart);
            when(productRepository.findById(deleteProductId)).thenReturn(
                Optional.of(deleteProduct));

            // when
            cartService.deleteCartProduct(userId, deleteProductId);

            // then
            assertThat(cart.getCartProducts()).noneMatch(
                cartProduct -> cartProduct.isSameProduct(deleteProduct));
            verify(cartRepository).persist(cart);
        }

        @Test
        @DisplayName("productId 에 해당하는 product 가 존재하지 않을 경우 NotExistsProductException 를 던진다.")
        void throwNotExistsProductExceptionWhenNotExitProduct() {
            // given
            final long userId = 1L;
            final long notExistProductId = 1L;

            final Cart cart = Cart(userId, new ArrayList<>());

            when(cartRepository.existCartByUserId(userId)).thenReturn(true);
            when(cartRepository.getByUserId(userId)).thenReturn(cart);
            when(productRepository.findById(notExistProductId)).thenReturn(
                Optional.empty());

            // when
            final Exception exception = catchException(
                () -> cartService.deleteCartProduct(userId, notExistProductId));

            // then
            assertThat(exception).isInstanceOf(NotExistsProductException.class);
        }

        @Test
        @DisplayName("Cart 가 존재하지 않는 경우 빈 Cart 를 생성하고 NotExistsCartProductException 를 던진다.")
        void createEmptyCartAndThrowNotExistsCartProductExceptionWhenNotExistCart() {
            // given
            final long userId = 1L;
            final long productId = 1L;

            final Product product = onSaleProduct(productId);
            final Cart emptyCart = emptyCart(userId);

            when(cartRepository.existCartByUserId(userId)).thenReturn(false);
            when(productRepository.findById(productId)).thenReturn(
                Optional.of(product));
            when(cartRepository.createEmptyCart(eq(userId), anyLong())).thenReturn(emptyCart);
            when(cartRepository.getByUserId(userId)).thenReturn(emptyCart);

            // when
            final Exception exception = catchException(
                () -> cartService.deleteCartProduct(userId, productId));

            // then
            assertThat(exception).isInstanceOf(NotExistsCartProductException.class);
        }
    }
}
