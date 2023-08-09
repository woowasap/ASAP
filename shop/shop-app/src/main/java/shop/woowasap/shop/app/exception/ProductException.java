package shop.woowasap.shop.app.exception;

public abstract class ProductException extends RuntimeException {

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
