package shop.woowasap.order.domain.in.response;

public record OrderProductResponse(long productId, String name, String price, long quantity) {

}
