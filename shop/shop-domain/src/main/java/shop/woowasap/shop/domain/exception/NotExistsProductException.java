package shop.woowasap.shop.domain.exception;

public class NotExistsProductException extends ProductException {

    public NotExistsProductException(String message) {
        super(message);
    }
}
