package shop.woowasap.shop.domain.exception;

public abstract class ProductException extends RuntimeException {

    protected ProductException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
