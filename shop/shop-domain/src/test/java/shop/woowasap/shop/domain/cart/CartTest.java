package shop.woowasap.shop.domain.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchException;
import static shop.woowasap.shop.domain.support.CartFixture.getCartProductBuilder;
import static shop.woowasap.shop.domain.support.CartFixture.getEmptyCartBuilder;
import static shop.woowasap.shop.domain.support.DomainFixture.getDefaultBuilder;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import shop.woowasap.shop.domain.exception.CannotFindProductInCartException;
import shop.woowasap.shop.domain.product.Product;
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
            final CartProduct cartProduct = CartProduct.builder()
                .product(getDefaultBuilder().build())
                .quantity(new CartProductQuantity(10L))
                .build();
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
            final CartProduct cartProduct = getCartProductBuilder().build();
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
    @DisplayName("deleteProduct 메서드는")
    class DeleteProduct_Method {

        @Test
        @DisplayName("cart 에 있는 product 를 제거한다.")
        void deleteProductInCart() {
            // given
            final CartProduct deleteCartProduct = getCartProductBuilder().build();
            final List<CartProduct> cartProducts = new ArrayList<>(List.of(deleteCartProduct));
            final Cart cart = getEmptyCartBuilder().cartProducts(cartProducts).build();

            // when
            cart.deleteProduct(deleteCartProduct.getProduct());

            // then
            assertThat(cart.getCartProducts())
                .noneMatch(cartProduct -> cartProduct
                    .isSameProduct(deleteCartProduct.getProduct()));
        }

        @Test
        @DisplayName("product 가 Cart 에 존재하지 않으면 CannotFindProductInCartException 을 던진다.")
        void throwCannotFindProductInCartExceptionWithNotExistInCart() {
            // given
            final Product product = getDefaultBuilder().build();
            final Cart cart = getEmptyCartBuilder().build();

            // when
            final Exception exception = catchException(() -> cart.deleteProduct(product));

            // then
            assertThat(exception).isInstanceOf(CannotFindProductInCartException.class);
        }
    }
}
