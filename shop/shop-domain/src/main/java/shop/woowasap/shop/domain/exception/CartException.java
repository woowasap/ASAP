package shop.woowasap.shop.domain.exception;

public class CartException extends RuntimeException {

    protected CartException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
