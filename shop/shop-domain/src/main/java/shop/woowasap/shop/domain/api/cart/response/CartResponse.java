package shop.woowasap.shop.domain.api.cart.response;

import java.util.List;

public record CartResponse(long cartId, List<CartProductResponse> cartProducts) {

}
