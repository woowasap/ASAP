package shop.woowasap.shop.domain.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchException;
import static shop.woowasap.shop.domain.support.CartFixture.getCartProductBuilder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import shop.woowasap.shop.domain.exception.InvalidCartProductQuantityException;
import shop.woowasap.shop.domain.product.Product;
import shop.woowasap.shop.domain.support.ProductFixture;

@DisplayName("CartProduct 테스트")
class CartProductTest {

    @Nested
    @DisplayName("장바구니 상품 생성 시")
    class newCartProductWithBuilder {

        @Test
        @DisplayName("정상적인 입력인 경우 장바구니 상품 생성한다.")
        void createCartProduct() {
            // when & then
            assertThatCode(
                () -> getCartProductBuilder().build()
            ).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("잘못된 상품 수량이 null로 입력되는 경우, InvalidCartProductQuantityException을 반환한다.")
        void throwExceptionWhenNullQuantity() {
            // given
            final Long nullQuantity = null;

            // when
            final Exception exception = catchException(
                () -> getCartProductBuilder().quantity(new CartProductQuantity(nullQuantity))
                    .build());

            // then
            assertThat(exception).isInstanceOf(InvalidCartProductQuantityException.class);
        }

        @Test
        @DisplayName("상품 수량이 10개를 초과하여 입력되는 경우 최대 구매 가능 상품 수량으로 고정된다.")
        void createCartWithOverMaxQuantity() {
            // given
            final Long overMaxQuantity = 11L;

            // when
            final CartProduct cartProduct = getCartProductBuilder().quantity(
                new CartProductQuantity(overMaxQuantity)).build();

            // then
            assertThat(cartProduct.getQuantity()).isEqualTo(new CartProductQuantity(10L));
        }

        @Test
        @DisplayName("상품 수량이 1개를 미만으로 입력되는 경우 최소 구매 가능 상품 수량으로 고정된다.")
        void createCartWithUnderMinQuantity() {
            // given
            final Long underMinQuantity = 0L;

            // when
            final CartProduct cartProduct = getCartProductBuilder().quantity(
                new CartProductQuantity(underMinQuantity)).build();

            // then
            assertThat(cartProduct.getQuantity()).isEqualTo(new CartProductQuantity(1L));
        }

        @Test
        @DisplayName("상품의 재고가 10개 이하인 경우 최대 구매 가능 상품 수량은 현재 상품의 재고로 고정된다.")
        void createCartWithProductRemainQuantity() {
            // given
            final Long productRemainQuantity = 8L;
            final Product product = ProductFixture.getDefaultBuilder()
                .quantity(productRemainQuantity).build();

            // when
            final CartProduct cartProduct = getCartProductBuilder(product, 10L).build();

            // then
            assertThat(cartProduct.getQuantity()).isEqualTo(
                new CartProductQuantity(productRemainQuantity));
        }
    }

    @Nested
    @DisplayName("장바구니 상품의 수량을 수정 시")
    class updateCartProductQuantity {

        @Test
        @DisplayName("상품의 수량이 변경된 상품이 생성된다.")
        void update() {
            // given
            final CartProductQuantity updateProductQuantity = new CartProductQuantity(20L);
            final CartProduct cartProduct = getCartProductBuilder().build();

            // when
            final CartProduct updateCartProduct = cartProduct.updateQuantity(updateProductQuantity);

            // then
            assertThat(updateCartProduct.getQuantity()).isEqualTo(updateProductQuantity);
        }
    }
}
