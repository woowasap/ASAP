package shop.woowasap.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.woowasap.shop.domain.api.cart.CartUseCase;
import shop.woowasap.shop.domain.api.cart.request.AddCartProductRequest;
import shop.woowasap.shop.domain.api.cart.response.CartResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/carts")
public class CartController {

    private static final long MOCK_USER_ID = 1L;

    private final CartUseCase cartUseCase;

    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        return ResponseEntity.ok(cartUseCase.getCartByUserId(MOCK_USER_ID));
    }

    @PostMapping
    public ResponseEntity<Void> createCartProduct(
        @RequestBody AddCartProductRequest addCartProductRequest) {
        cartUseCase.addCartProduct(MOCK_USER_ID, addCartProductRequest);

        return ResponseEntity.ok().build();
    }

}
