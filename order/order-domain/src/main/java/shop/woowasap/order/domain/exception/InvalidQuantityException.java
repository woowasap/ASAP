package shop.woowasap.order.domain.exception;

import java.text.MessageFormat;

public class InvalidQuantityException extends RuntimeException {

    public InvalidQuantityException(int quantity) {
        super(MessageFormat.format("quantity \"{0}\" 는 0 이하가 될 수 없습니다.", quantity));
    }
}
