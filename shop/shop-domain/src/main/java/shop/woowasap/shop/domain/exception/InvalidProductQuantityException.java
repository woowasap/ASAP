package shop.woowasap.shop.domain.exception;

public class InvalidProductQuantityException extends ProductException {

    public InvalidProductQuantityException(final String message) {
        super(message);
    }
}
