package shop.woowasap.shop.domain.exception;

public class InvalidCartProductQuantityException extends CartException {

    public InvalidCartProductQuantityException(final String message) {
        super(message);
    }
}
