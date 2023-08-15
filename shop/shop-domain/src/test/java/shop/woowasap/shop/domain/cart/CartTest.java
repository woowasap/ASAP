package shop.woowasap.shop.domain.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchException;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import shop.woowasap.shop.domain.exception.NotExistsCartProductException;
import shop.woowasap.shop.domain.support.CartFixture;

@DisplayName("Cart 테스트")
class CartTest {

    @Nested
    @DisplayName("장바구니 생성 시")
    class newCartWithBuilder {

        @Test
        @DisplayName("정상적인 입력인 경우 장바구니를 생성한다.")
        void createCart() {
            // when & then
            assertThatCode(() -> CartFixture.getEmptyCartBuilder().build())
                .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("장바구니에 상품을 추가하는 경우")
    class addCartProduct {

        @Test
        @DisplayName("해당 상품이 없는 경우 장바구니에 추가된다.")
        void addCartProductWithNotExists() {
            // given
            final CartProduct cartProduct = CartFixture.getCartProductBuilder().build();
            final Cart cart = CartFixture.getEmptyCartBuilder().build();

            // when
            cart.addProduct(cartProduct);

            // then
            assertThat(cart.getCartProducts()).containsExactly(cartProduct);
        }

        @Test
        @DisplayName("해당 상품이 있는 경우 장바구니의 해당 상품의 개수가 증가한다.")
        void addCartProductWithExists() {
            // given
            final long quantity = 10L;
            final CartProduct cartProduct = CartFixture.getCartProductBuilder().build();
            final List<CartProduct> cartProducts = new ArrayList<>();
            cartProducts.add(cartProduct);
            final Cart cart = CartFixture.getEmptyCartBuilder()
                .cartProducts(cartProducts).build();

            // when
            cart.addProduct(cartProduct);

            // then
            assertThat(cart.getCartProducts()).hasSize(1);
            assertThat(cart.getCartProducts().get(0).getQuantity()).isEqualTo(
                new CartProductQuantity(quantity + quantity));
        }
    }

    @Nested
    @DisplayName("장바구니에 상품의 수량을 수정하는 경우")
    class updateCartProductQuantity {

        @Test
        @DisplayName("해당 상품이 없으면, 예외를 던진다.")
        void updateCartProductQuantityWithNotExistsCartProductThrowException() {
            // given
            final CartProduct cartProduct = CartFixture.getCartProductBuilder().build();
            final CartProductQuantity updateCartProductQuantity = new CartProductQuantity(20L);
            final Cart cart = CartFixture.getEmptyCartBuilder().build();

            // when
            final Exception exception = catchException(
                () -> cart.updateCartProduct(cartProduct, updateCartProductQuantity));

            // then
            assertThat(exception).isInstanceOf(NotExistsCartProductException.class);
        }

        @Test
        @DisplayName("정상적으로 개수가 변경된다.")
        void updateCartProductQuantityWithExists() {
            // given
            final CartProduct cartProduct = CartFixture.getCartProductBuilder().build();
            final List<CartProduct> cartProducts = new ArrayList<>();
            cartProducts.add(cartProduct);
            final CartProductQuantity updateCartProductQuantity = new CartProductQuantity(20L);
            final Cart cart = CartFixture.getEmptyCartBuilder().cartProducts(cartProducts).build();

            // when
            cart.updateCartProduct(cartProduct, updateCartProductQuantity);

            // then
            assertThat(cart.getCartProducts()).hasSize(1);
            assertThat(cart.getCartProducts().get(0).getQuantity()).isEqualTo(updateCartProductQuantity);
        }
    }
}
