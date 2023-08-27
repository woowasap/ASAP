package shop.woowasap.shop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import shop.woowasap.core.id.api.IdGenerator;
import shop.woowasap.shop.domain.cart.Cart;
import shop.woowasap.shop.domain.cart.CartProduct;
import shop.woowasap.shop.domain.in.cart.CartConnector;
import shop.woowasap.shop.domain.out.CartRepository;
import shop.woowasap.shop.service.support.fixture.CartFixture;

@ExtendWith(SpringExtension.class)
@DisplayName("CartConnectorService 클래스")
@ContextConfiguration(classes = CartConnectorService.class)
class CartConnectorServiceTest {

    @Autowired
    private CartConnector cartConnector;

    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private IdGenerator idGenerator;

    @Nested
    @DisplayName("findByCartIdAndUserId 메소드는")
    class findByCartIdAndUserIdMethod {

        @Test
        @DisplayName("cartId와 userId가 일치하는 Cart가 있다면 Cart를 반환한다.")
        void returnCartWhenExistMatchedCart() {
            // given
            final long cartId = 1L;
            final long userId = 2L;
            final Cart expected = CartFixture.getEmptyCartBuilder()
                .id(cartId)
                .userId(userId)
                .build();

            when(cartRepository.getByUserId(userId)).thenReturn(expected);

            // when
            final Optional<Cart> result = cartConnector.findByCartIdAndUserId(cartId, userId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        @DisplayName("cartId와 userId가 일치하는 Cart가 없다면, Optional.empty를 반환한다.")
        void returnEmptyWhenCannotFindMatchedCart() {
            // given
            final long cartId = 1L;
            final long userId = 2L;

            when(cartRepository.getByUserId(userId)).thenThrow(
                new InvalidDataAccessApiUsageException("test"));

            // when
            final Optional<Cart> result = cartConnector.findByCartIdAndUserId(cartId, userId);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("조회된 cart의 cartId와 파라미터로 받은 cartId가 다르다면, Optional.empty를 반환한다.")
        void returnEmptyWhenCartIdDoesNotMatch() {
            // given
            final long cartId = 1L;
            final long userId = 2L;
            final Cart cart = CartFixture.getEmptyCartBuilder()
                .id(cartId)
                .userId(userId)
                .build();

            when(cartRepository.getByUserId(userId)).thenReturn(cart);

            final long invalidCartId = 999L;

            // when
            final Optional<Cart> result = cartConnector.findByCartIdAndUserId(invalidCartId,
                userId);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("clearCartByUserId 메서드는")
    class ClearCartByUserIdMethod {

        @Test
        @DisplayName("userId 에 해당하는 Cart 를 초기화한다.")
        void clearCartWithUserId() {
            // given
            final long userId = 1L;
            final CartProduct cartProduct = CartFixture.cartProduct();
            final Cart cart = CartFixture.Cart(userId, new ArrayList<>(List.of(cartProduct)));

            when(cartRepository.getByUserId(userId)).thenReturn(cart);

            // when
            cartConnector.clearCartByUserId(userId);

            // then
            assertThat(cart.getCartProducts()).isEmpty();
        }

        @Test
        @DisplayName("userId 에 해당하는 Cart 가 없다면, 빈 Cart 를 생성한다.")
        void createEmptyCartWhenNotExistsCart() {
            // given
            final long userId = 1L;

            when(cartRepository.getByUserId(userId)).thenThrow(
                InvalidDataAccessApiUsageException.class);

            // when
            cartConnector.clearCartByUserId(userId);

            // then
            verify(cartRepository).createEmptyCart(eq(userId), anyLong());
        }
    }
}
