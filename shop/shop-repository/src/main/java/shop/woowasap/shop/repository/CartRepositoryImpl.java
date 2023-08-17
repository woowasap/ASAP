package shop.woowasap.shop.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.woowasap.shop.domain.cart.Cart;
import shop.woowasap.shop.domain.spi.CartRepository;
import shop.woowasap.shop.repository.entity.CartEntity;
import shop.woowasap.shop.repository.jpa.CartJpaRepository;

@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepository {

    private final CartJpaRepository cartJpaRepository;

    @Override
    public Cart createEmptyCart(final long userId, final long cartId) {
        final CartEntity persist = cartJpaRepository.save(new CartEntity(cartId, userId));

        return persist.toDomain();
    }

    @Override
    public Cart persist(final Cart cart) {
        final CartEntity cartEntity = cartJpaRepository.save(CartEntity.from(cart));

        return cartEntity.toDomain();
    }

    @Override
    public boolean existCartByUserId(final long userId) {
        return cartJpaRepository.existsByUserId(userId);
    }

    @Override
    public Cart getByUserId(final long userId) {
        return cartJpaRepository.findByUserId(userId)
            .orElseThrow(IllegalStateException::new).toDomain();
    }
}
