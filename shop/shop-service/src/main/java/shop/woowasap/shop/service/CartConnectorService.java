package shop.woowasap.shop.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.shop.domain.api.cart.CartConnector;
import shop.woowasap.shop.domain.cart.Cart;
import shop.woowasap.shop.domain.spi.CartRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartConnectorService implements CartConnector {

    private final CartRepository cartRepository;

    @Override
    public Optional<Cart> findByCartIdAndUserId(final long cartId, final long userId) {
        try {
            final Cart cart = cartRepository.getByUserId(userId);
            if (!cart.getId().equals(cartId)) {
                return Optional.empty();
            }
            return Optional.of(cart);
        } catch (IllegalStateException illegalStateException) {
            return Optional.empty();
        }
    }
}
