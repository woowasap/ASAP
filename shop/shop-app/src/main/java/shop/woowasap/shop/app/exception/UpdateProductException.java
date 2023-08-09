package shop.woowasap.shop.app.exception;

public class UpdateProductException extends RuntimeException {

    public UpdateProductException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
