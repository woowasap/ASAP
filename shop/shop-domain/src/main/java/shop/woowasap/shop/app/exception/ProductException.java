package shop.woowasap.shop.app.exception;

public abstract class ProductException extends RuntimeException {

    protected ProductException() {
    }

    protected ProductException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
