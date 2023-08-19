package shop.woowasap.shop.domain.in.cart.response;

public record CartProductResponse(long productId, String name, String price, long quantity,
                                  long productRemainQuantity) {

}
