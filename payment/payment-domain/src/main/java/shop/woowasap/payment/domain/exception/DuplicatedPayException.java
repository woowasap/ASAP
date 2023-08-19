package shop.woowasap.payment.domain.exception;

import java.text.MessageFormat;

public class DuplicatedPayException extends RuntimeException {

    private static final String MESSAGE = "해당하는 주문의 결제가 이미 있습니다. 입력 값 : '{0}'";

    public DuplicatedPayException(long orderId) {
        super(MessageFormat.format(MESSAGE, orderId));
    }

}
