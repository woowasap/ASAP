package shop.woowasap.payment.domain.exception;

public class PayUserNotMatchException extends RuntimeException {

    private static final String MESSAGE = "해당 유저의 결제가 아닙니다.";

    public PayUserNotMatchException() {
        super(MESSAGE);
    }
}
