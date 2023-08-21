package shop.woowasap.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.woowasap.core.util.web.ErrorTemplate;
import shop.woowasap.shop.domain.in.cart.CartUseCase;
import shop.woowasap.shop.domain.in.cart.request.AddCartProductRequest;
import shop.woowasap.shop.domain.in.cart.request.UpdateCartProductRequest;
import shop.woowasap.shop.domain.in.cart.response.CartResponse;
import shop.woowasap.shop.domain.exception.NotExistsProductException;
import shop.woowasap.shop.domain.exception.NotExistsCartProductException;
import shop.woowasap.auth.domain.in.LoginUser;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/carts")
public class CartController {

    private final CartUseCase cartUseCase;

    @GetMapping
    public ResponseEntity<CartResponse> getCart(@LoginUser final Long userId) {
        return ResponseEntity.ok(cartUseCase.getCartByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<Void> createCartProduct(
        @RequestBody final AddCartProductRequest addCartProductRequest,
        @LoginUser final Long userId) {
        cartUseCase.addCartProduct(userId, addCartProductRequest);

        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<Void> updateCartProduct(
        @RequestBody final UpdateCartProductRequest updateCartProductRequest,
        @LoginUser final Long userId) {
        cartUseCase.updateCartProduct(userId, updateCartProductRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCartProduct(@RequestParam("product-id") final long productId,
        @LoginUser final Long userId) {
        cartUseCase.deleteCartProduct(userId, productId);

        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({NotExistsCartProductException.class, NotExistsProductException.class})
    public ResponseEntity<ErrorTemplate> handleException(final Exception exception) {
        return ResponseEntity.badRequest()
            .body(ErrorTemplate.of(exception.getMessage()));
    }
}
