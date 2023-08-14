package shop.woowasap.order.domain.exception;

import java.text.MessageFormat;

public final class DoesNotFindProductException extends RuntimeException{

    public DoesNotFindProductException(long productId) {
        super(MessageFormat.format("productId \"{0}\" 에 해당하는 Product를 찾을 수 없습니다.", productId));
    }
}
