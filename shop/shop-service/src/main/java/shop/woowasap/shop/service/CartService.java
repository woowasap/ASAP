package shop.woowasap.shop.service;

import static shop.woowasap.shop.service.mapper.CartMapper.toCartResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.core.id.api.IdGenerator;
import shop.woowasap.shop.domain.api.cart.CartUseCase;
import shop.woowasap.shop.domain.api.cart.request.AddCartProductRequest;
import shop.woowasap.shop.domain.api.cart.response.CartResponse;
import shop.woowasap.shop.domain.api.product.request.UpdateProductRequest;
import shop.woowasap.shop.domain.cart.Cart;
import shop.woowasap.shop.domain.spi.CartRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService implements CartUseCase {

    private final CartRepository cartRepository;
    private final IdGenerator idGenerator;

    @Override
    public void updateCartProduct(final long userId,
        final UpdateProductRequest updateProductRequest) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addCartProduct(final long userId,
        final AddCartProductRequest addCartProductRequest) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteCartProduct(final long userId, final long productId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CartResponse getCartByUserId(final long userId) {
        if (cartRepository.existCartByUserId(userId)) {
            final Cart emptyCart = cartRepository.createEmptyCart(userId, idGenerator.generate());
            cartRepository.persist(emptyCart);
        }

        return toCartResponse(cartRepository.getByUserId(userId));
    }
}
