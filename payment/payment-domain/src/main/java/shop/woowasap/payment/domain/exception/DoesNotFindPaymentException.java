package shop.woowasap.payment.domain.exception;

import java.text.MessageFormat;

public class DoesNotFindPaymentException extends RuntimeException {

    private static final String MESSAGE = "해당하는 주문의 결제가 없습니다. 입력 값 : '{0}'";

    public DoesNotFindPaymentException(long orderId) {
        super(MessageFormat.format(MESSAGE, orderId));
    }
}
