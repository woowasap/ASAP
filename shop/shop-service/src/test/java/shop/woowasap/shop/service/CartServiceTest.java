package shop.woowasap.shop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static shop.woowasap.shop.service.support.fixture.CartFixture.Cart;
import static shop.woowasap.shop.service.support.fixture.CartFixture.cartProduct;
import static shop.woowasap.shop.service.support.fixture.CartFixture.cartResponse;

import java.util.List;
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
import shop.woowasap.shop.domain.cart.CartProduct;
import shop.woowasap.shop.domain.spi.CartRepository;

@ExtendWith(SpringExtension.class)
@DisplayName("CartService 클래스")
@ContextConfiguration(classes = CartService.class)
public class CartServiceTest {

    @Autowired
    private CartService cartService;

    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private IdGenerator idGenerator;

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
    }

}
