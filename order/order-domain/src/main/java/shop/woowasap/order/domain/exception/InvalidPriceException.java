package shop.woowasap.order.domain.exception;

import java.text.MessageFormat;

public final class InvalidPriceException extends RuntimeException {

    public InvalidPriceException(String price) {
        super(MessageFormat.format("가격으로 할당될 수 없는 값 \"{0}\" 입니다.", price));
    }

}
