package shop.woowasap.order.domain.exception;

import java.text.MessageFormat;

public class BlankProductNameException extends RuntimeException {

    public BlankProductNameException() {
        super("OrderProduct의 이름이 공백이거나 null 입니다.");
    }
}
