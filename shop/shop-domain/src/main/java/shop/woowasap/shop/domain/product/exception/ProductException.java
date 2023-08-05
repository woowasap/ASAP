package shop.woowasap.shop.domain.product.exception;

public abstract class ProductException extends RuntimeException {

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
