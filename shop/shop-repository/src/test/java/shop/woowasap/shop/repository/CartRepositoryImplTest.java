package shop.woowasap.shop.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import shop.woowasap.BeanScanBaseLocation;
import shop.woowasap.shop.domain.cart.Cart;
import shop.woowasap.shop.domain.cart.CartProduct;
import shop.woowasap.shop.domain.cart.CartProductQuantity;
import shop.woowasap.shop.domain.product.Product;
import shop.woowasap.shop.domain.product.SaleTime;
import shop.woowasap.shop.repository.entity.ProductEntity;
import shop.woowasap.shop.repository.jpa.ProductJpaRepository;

@DataJpaTest
@DisplayName("CartRepositoryImpl 클래스")
@ContextConfiguration(classes = {BeanScanBaseLocation.class, CartRepositoryImpl.class})
class CartRepositoryImplTest {

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private CartRepositoryImpl cartRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        final Instant startTime = Instant.now().minusSeconds(100);
        final Instant endTime = Instant.now().plusSeconds(100);
        final SaleTime saleTime = SaleTime.builder()
            .startTime(startTime)
            .endTime(endTime)
            .build();

        product = Product.builder()
            .id(1L)
            .name("name")
            .description("description")
            .price("10000")
            .quantity(1000L)
            .saleTime(saleTime)
            .build();

        productJpaRepository.save(ProductEntity.from(product));
    }

    @Nested
    @DisplayName("빈 장바구니를 생성하는 메서드는")
    class CreateEmptyCart {

        @Test
        @DisplayName("빈 장바구니를 생성한다.")
        void createEmptyCart() {
            // given
            final long userId = 1L;
            final long cartId = 1L;
            final Cart expected = new Cart(cartId, userId, new ArrayList<>());

            // when
            final Cart emptyCart = cartRepository.createEmptyCart(userId, cartId);

            // then
            assertThat(emptyCart).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        @DisplayName("기존의 장바구니가 존재했다면, 장바구니가 초기화된다.")
        void clearCart() {
            // given
            final long userId = 1L;
            final long cartId = 1L;

            final CartProduct cartProduct = CartProduct.builder().product(product)
                .quantity(new CartProductQuantity(10L))
                .build();

            cartRepository.persist(new Cart(cartId, userId, List.of(cartProduct)));

            // when
            cartRepository.createEmptyCart(userId, cartId);
            final Cart cart = cartRepository.getByUserId(userId);

            // then
            assertThat(cart.getCartProducts()).isEmpty();
        }
    }

    @Nested
    @DisplayName("persist 메서드는")
    class Persist {

        @Test
        @DisplayName("장바구니와 장바구니 상품을 저장한다.")
        void save() {
            // given
            final long cartId = 1L;
            final long userId = 1L;

            final Cart cart = new Cart(cartId, userId, List.of(
                CartProduct.builder().product(product).quantity(new CartProductQuantity(10L))
                    .build()));

            // when
            final Cart persistCart = cartRepository.persist(cart);

            // then
            assertThat(persistCart).usingRecursiveComparison().isEqualTo(cart);
        }

        @Test
        @DisplayName("장바구니에 담긴 장바구니 상품을 삭제 한다.")
        void delete() {
            // given
            final long userId = 1L;
            final long cartId = 1L;

            cartRepository.persist(new Cart(cartId, userId, List.of(
                CartProduct.builder().product(product).quantity(new CartProductQuantity(10L))
                    .build())));
            final Cart deleted = new Cart(cartId, userId, new ArrayList<>());

            // when
            final Cart persist = cartRepository.persist(deleted);

            // then
            assertThat(persist).usingRecursiveComparison().isEqualTo(deleted);
        }

        @Test
        @DisplayName("장바구니에 담긴 장바구니 상품의 수량을 수정 한다.")
        void update() {
            // given
            final long userId = 1L;
            final long cartId = 1L;

            cartRepository.persist(new Cart(cartId, userId, List.of(
                CartProduct.builder().product(product).quantity(new CartProductQuantity(10L))
                    .build())));

            final Cart update = new Cart(cartId, userId, List.of(
                CartProduct.builder().product(product).quantity(new CartProductQuantity(20L))
                    .build()));

            // when
            final Cart persist = cartRepository.persist(update);

            // then
            assertThat(persist).usingRecursiveComparison().isEqualTo(update);
        }
    }

    @Nested
    @DisplayName("ExistCartByUserId 메서드는")
    class ExistCartByUserId {

        @Test
        @DisplayName("해당 회원의 장바구니가 없는 경우 false를 반환한다.")
        void existCartByUserIdReturnFalse() {
            // given
            final long notExistsCartUserId = 2L;

            // when & then
            assertThat(cartRepository.existCartByUserId(notExistsCartUserId)).isFalse();
        }

        @Test
        @DisplayName("해당 회원의 장바구니가 있는 경우 true를 반환한다.")
        void existCartByUserIdReturnTrue() {
            // given
            final long userId = 1L;
            final long cartId = 1L;
            cartRepository.persist(new Cart(cartId, userId, new ArrayList<>()));

            // when & then
            assertThat(cartRepository.existCartByUserId(userId)).isTrue();
        }
    }

    @Nested
    @DisplayName("GetByUserId 메서드는")
    class GetByUserId {

        @Test
        @DisplayName("해당 회원의 장바구니가 있는 경우 장바구니를 반환한다.")
        void getByUserId() {
            // given
            final long userId = 1L;
            final long cartId = 1L;
            final Cart expected = new Cart(cartId, userId, new ArrayList<>());
            cartRepository.persist(expected);

            // when
            final Cart persist = cartRepository.getByUserId(userId);

            // then
            assertThat(persist).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        @DisplayName("해당 회원의 장바구니가 없는 경우 IllegalStateException을 던진다.")
        void getByUserIdThrowException() {
            // given
            final long notExistsCartUserId = 2L;

            // when
            final Exception exception = catchException(
                () -> cartRepository.getByUserId(notExistsCartUserId));

            // then
            assertThat(exception).isInstanceOf(IllegalStateException.class);
        }
    }
}
