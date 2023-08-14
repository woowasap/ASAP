package shop.woowasap.order.domain.in.request;

public record OrderProductRequest(long userId, long productId, long quantity) {
}
