package shop.woowasap.shop.service;

import static shop.woowasap.shop.service.mapper.CartMapper.toCartResponse;

import java.text.MessageFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.core.id.api.IdGenerator;
import shop.woowasap.shop.domain.api.cart.CartUseCase;
import shop.woowasap.shop.domain.api.cart.request.AddCartProductRequest;
import shop.woowasap.shop.domain.api.cart.request.UpdateCartProductRequest;
import shop.woowasap.shop.domain.api.cart.response.CartResponse;
import shop.woowasap.shop.domain.cart.Cart;
import shop.woowasap.shop.domain.cart.CartProduct;
import shop.woowasap.shop.domain.cart.CartProductQuantity;
import shop.woowasap.shop.domain.exception.CannotFindProductException;
import shop.woowasap.shop.domain.product.Product;
import shop.woowasap.shop.domain.spi.CartRepository;
import shop.woowasap.shop.domain.spi.ProductRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService implements CartUseCase {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final IdGenerator idGenerator;

    @Override
    @Transactional
    public void updateCartProduct(final long userId,
        final UpdateCartProductRequest updateCartProductRequest) {
        if (!cartRepository.existCartByUserId(userId)) {
            cartRepository.createEmptyCart(userId, idGenerator.generate());
        }

        final Cart cart = cartRepository.getByUserId(userId);
        final Product product = getByProductId(updateCartProductRequest.productId());

        cart.updateCartProduct(CartProduct.builder()
            .product(product)
            .quantity(new CartProductQuantity(updateCartProductRequest.quantity()))
            .build());
        cartRepository.persist(cart);
    }

    @Override
    @Transactional
    public void addCartProduct(final long userId,
        final AddCartProductRequest addCartProductRequest) {
        if (!cartRepository.existCartByUserId(userId)) {
            cartRepository.createEmptyCart(userId, idGenerator.generate());
        }

        final Cart cart = cartRepository.getByUserId(userId);
        final Product product = getByProductId(addCartProductRequest.productId());

        cart.addProduct(CartProduct.builder()
            .product(product)
            .quantity(new CartProductQuantity(addCartProductRequest.quantity()))
            .build());
        cartRepository.persist(cart);
    }

    @Override
    public void deleteCartProduct(final long userId, final long productId) {
        if (!cartRepository.existCartByUserId(userId)) {
            cartRepository.createEmptyCart(userId, idGenerator.generate());
        }

        final Cart cart = cartRepository.getByUserId(userId);
        final Product product = getByProductId(productId);

        cart.deleteProduct(product);
        cartRepository.persist(cart);
    }

    @Override
    public CartResponse getCartByUserId(final long userId) {
        if (!cartRepository.existCartByUserId(userId)) {
            final Cart emptyCart = cartRepository.createEmptyCart(userId, idGenerator.generate());
            cartRepository.persist(emptyCart);
        }

        return toCartResponse(cartRepository.getByUserId(userId));
    }

    private Product getByProductId(final Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new CannotFindProductException(
                MessageFormat.format("productId 에 해당하는 Product 가 존재하지 않습니다. productId : \"{0}\"",
                    productId)
            ));
    }
}
