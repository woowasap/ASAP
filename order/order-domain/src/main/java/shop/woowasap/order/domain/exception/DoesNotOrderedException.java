package shop.woowasap.order.domain.exception;

public class DoesNotOrderedException extends RuntimeException {

    public DoesNotOrderedException() {
        super("결제에 실패했습니다.");
    }
}
