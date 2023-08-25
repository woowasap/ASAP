package shop.woowasap.shop.domain.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchException;
import static shop.woowasap.shop.domain.support.CartFixture.getCartProductBuilder;
import static shop.woowasap.shop.domain.support.CartFixture.getEmptyCartBuilder;
import static shop.woowasap.shop.domain.support.ProductFixture.getDefaultBuilder;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import shop.woowasap.shop.domain.exception.NotExistsCartProductException;
import shop.woowasap.shop.domain.product.Product;

@DisplayName("Cart 테스트")
class CartTest {

    @Nested
    @DisplayName("장바구니 생성 시")
    class newCartWithBuilder {

        @Test
        @DisplayName("정상적인 입력인 경우 장바구니를 생성한다.")
        void createCart() {
            // when & then
            assertThatCode(() -> getEmptyCartBuilder().build())
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
            final CartProduct cartProduct = getCartProductBuilder().build();
            final Cart cart = getEmptyCartBuilder().build();

            // when
            cart.addProduct(cartProduct);

            // then
            assertThat(cart.getCartProducts()).containsExactly(cartProduct);
        }

        @Test
        @DisplayName("해당 상품의 재고가 장바구니에 담으려는 재고보다 적고, 해당 상품이 장바구니에 없는 경우 장바구니에 상품의 재고 만큼 추가된다.")
        void addCartProductWithNotExistsWithMaxProductQuantity() {
            // given
            final long remainQuantity = 5L;
            final Product product = getDefaultBuilder().quantity(remainQuantity).build();
            final CartProduct cartProduct = getCartProductBuilder(product, 10L).build();
            final Cart cart = getEmptyCartBuilder().build();

            // when
            cart.addProduct(cartProduct);

            // then
            assertThat(cart.getCartProducts()).hasSize(1);
            assertThat(cart.getCartProducts().get(0).getQuantity().getValue()).isEqualTo(5L);
        }

        @Test
        @DisplayName("해당 상품이 있는 경우 장바구니의 해당 상품의 개수가 증가한다.")
        void addCartProductWithExists() {
            // given
            final long quantity = 3L;
            final CartProduct cartProduct = getCartProductBuilder().quantity(
                new CartProductQuantity(quantity)).build();
            final List<CartProduct> cartProducts = new ArrayList<>();
            cartProducts.add(cartProduct);
            final Cart cart = getEmptyCartBuilder()
                .cartProducts(cartProducts).build();

            // when
            cart.addProduct(cartProduct);

            // then
            assertThat(cart.getCartProducts()).hasSize(1);
            assertThat(cart.getCartProducts().get(0).getQuantity()).isEqualTo(
                new CartProductQuantity(quantity + quantity));
        }

        @Test
        @DisplayName("상품의 재고가 기존에 장바구니에 담겨져 있는 재고와 합쳐지는 재고의 양보다 적으면, 상품의 최대 재고로 변경된다.")
        void addCartProductQuantityWithMaxProductQuantity() {
            // given
            final long remainQuantity = 5L;
            final Product product = getDefaultBuilder().quantity(remainQuantity)
                .build();
            final CartProduct cartProduct = getCartProductBuilder(product, 4L).build();
            final List<CartProduct> cartProducts = new ArrayList<>();
            cartProducts.add(cartProduct);
            final Cart cart = getEmptyCartBuilder().cartProducts(cartProducts).build();

            // when
            cart.addProduct(cartProduct);

            // then
            assertThat(cart.getCartProducts()).hasSize(1);
            assertThat(cart.getCartProducts().get(0).getQuantity()).isEqualTo(
                new CartProductQuantity(remainQuantity));
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
        @DisplayName("product 가 Cart 에 존재하지 않으면 NotExistsCartProductException 을 던진다.")
        void throwNotExistsCartProductExceptionWithNotExistInCart() {
            // given
            final Product product = getDefaultBuilder().build();
            final Cart cart = getEmptyCartBuilder().build();

            // when
            final Exception exception = catchException(() -> cart.deleteProduct(product));

            // then
            assertThat(exception).isInstanceOf(NotExistsCartProductException.class);
        }
    }

    @Nested
    @DisplayName("장바구니에 상품의 수량을 수정하는 경우")
    class updateCartProductQuantity {

        @Test
        @DisplayName("해당 상품이 없으면, 예외를 던진다.")
        void updateCartProductQuantityWithNotExistsCartProductThrowException() {
            // given
            final CartProductQuantity updateCartProductQuantity = new CartProductQuantity(20L);
            final CartProduct cartProduct = getCartProductBuilder()
                .quantity(updateCartProductQuantity).build();
            final Cart cart = getEmptyCartBuilder().build();

            // when
            final Exception exception = catchException(
                () -> cart.updateCartProduct(cartProduct));

            // then
            assertThat(exception).isInstanceOf(NotExistsCartProductException.class);
        }

        @Test
        @DisplayName("정상적으로 개수가 변경된다.")
        void updateCartProductQuantityWithExists() {
            // given
            final List<CartProduct> cartProducts = new ArrayList<>();
            cartProducts.add(getCartProductBuilder().build());
            final CartProductQuantity updateCartProductQuantity = new CartProductQuantity(5L);
            final CartProduct updateCartProduct = getCartProductBuilder()
                .quantity(updateCartProductQuantity).build();
            final Cart cart = getEmptyCartBuilder().cartProducts(cartProducts).build();

            // when
            cart.updateCartProduct(updateCartProduct);

            // then
            assertThat(cart.getCartProducts()).hasSize(1);
            assertThat(cart.getCartProducts().get(0).getQuantity()).isEqualTo(
                updateCartProductQuantity);
        }

        @Test
        @DisplayName("상품의 재고가 바꾸려는 개수보다 적으면, 상품의 최대 재고로 변경된다.")
        void updateCartProductQuantityWithMaxProductQuantity() {
            // given
            final long remainQuantity = 5L;
            final Product product = getDefaultBuilder().quantity(remainQuantity)
                .build();
            final List<CartProduct> cartProducts = new ArrayList<>();
            cartProducts.add(getCartProductBuilder(product, 3L).build());
            final CartProduct updateCartProduct = getCartProductBuilder(product, 10L).build();
            final Cart cart = getEmptyCartBuilder().cartProducts(cartProducts).build();

            // when
            cart.updateCartProduct(updateCartProduct);

            // then
            assertThat(cart.getCartProducts()).hasSize(1);
            assertThat(cart.getCartProducts().get(0).getQuantity().getValue())
                .isEqualTo(remainQuantity);
        }
    }
}
