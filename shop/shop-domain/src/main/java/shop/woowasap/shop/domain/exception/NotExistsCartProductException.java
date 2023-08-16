package shop.woowasap.shop.domain.exception;

public class NotExistsCartProductException extends CartException {

    public NotExistsCartProductException(final String message) {
        super(message);
    }
}
