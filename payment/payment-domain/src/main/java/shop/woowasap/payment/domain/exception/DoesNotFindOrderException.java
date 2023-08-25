package shop.woowasap.payment.domain.exception;

import java.text.MessageFormat;

public final class DoesNotFindOrderException extends RuntimeException {

    public DoesNotFindOrderException(final long orderId, final long userId) {
        super(MessageFormat.format("orderId \"{0}\"와 userId \"{1}\" 에 해당하는 order 를 찾을 수 없습니다.",
            orderId, userId));
    }
}
