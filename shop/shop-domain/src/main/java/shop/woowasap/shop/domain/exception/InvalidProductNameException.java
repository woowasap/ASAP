package shop.woowasap.shop.domain.exception;

public class InvalidProductNameException extends ProductException {

    public InvalidProductNameException(final String message) {
        super(message);
    }
}
