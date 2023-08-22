package shop.woowasap.shop.domain.exception;

public class InvalidProductPriceException extends ProductException {

    public InvalidProductPriceException(final String message) {
        super(message);
    }
}
