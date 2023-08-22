package shop.woowasap.shop.domain.exception;

public class InvalidProductDescriptionException extends ProductException {

    public InvalidProductDescriptionException(final String message) {
        super(message);
    }
}
