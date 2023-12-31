package shop.woowasap.shop.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.core.id.api.IdGenerator;
import shop.woowasap.shop.domain.cart.Cart;
import shop.woowasap.shop.domain.in.cart.CartConnector;
import shop.woowasap.shop.domain.out.CartRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartConnectorService implements CartConnector {

    private final CartRepository cartRepository;
    private final IdGenerator idGenerator;

    @Override
    public Optional<Cart> findByCartIdAndUserId(final long cartId, final long userId) {
        try {
            final Cart cart = cartRepository.getByUserId(userId);
            if (!cart.getId().equals(cartId)) {
                return Optional.empty();
            }
            return Optional.of(cart);
        } catch (InvalidDataAccessApiUsageException invalidDataAccessApiUsageException) {
            return Optional.empty();
        }
    }

    @Override
    public void clearCartByUserId(final long userId) {
        try {
            final Cart cart = cartRepository.getByUserId(userId);
            cart.clear();
            cartRepository.persist(cart);
        } catch (InvalidDataAccessApiUsageException invalidDataAccessApiUsageException) {
            cartRepository.createEmptyCart(userId, idGenerator.generate());
        }
    }
}
