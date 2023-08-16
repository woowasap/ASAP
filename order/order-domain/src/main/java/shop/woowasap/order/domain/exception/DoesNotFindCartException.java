package shop.woowasap.order.domain.exception;

import java.text.MessageFormat;

public final class DoesNotFindCartException extends RuntimeException {

    public DoesNotFindCartException(final long cartId, final long userId) {
        super(MessageFormat.format("cartId \"{0}\" 와 userId \"{1}\" 에 해당하는 Cart를 찾을 수 없습니다.", cartId, userId));
    }
}
