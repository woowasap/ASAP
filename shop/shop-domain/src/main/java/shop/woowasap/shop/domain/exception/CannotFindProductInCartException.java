package shop.woowasap.shop.domain.exception;

public class CannotFindProductInCartException extends CartException {

    public CannotFindProductInCartException(final String message) {
        super(message);
    }
}
