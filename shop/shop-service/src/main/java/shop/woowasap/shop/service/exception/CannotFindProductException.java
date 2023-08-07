package shop.woowasap.shop.service.exception;

import java.text.MessageFormat;

public final class CannotFindProductException extends RuntimeException {

    public CannotFindProductException(long productId) {
        super(MessageFormat.format("productId \"{0}\" 에 해당하는 product를 찾을 수 없습니다.", productId));
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
