package shop.woowasap.shop.domain.in.cart.response;

import java.util.List;

public record CartResponse(long cartId, List<CartProductResponse> cartProducts) {

}
