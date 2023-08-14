package shop.woowasap.shop.domain.exception;

public class CannotFindProductException extends ProductException {

    public CannotFindProductException(String message) {
        super(message);
    }
}
