package shop.woowasap.order.domain.in.event;

public record PaySuccessEvent(long orderId, long userId) {
}
