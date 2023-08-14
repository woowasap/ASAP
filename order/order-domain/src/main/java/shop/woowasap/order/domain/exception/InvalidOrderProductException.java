package shop.woowasap.order.domain.exception;

import java.text.MessageFormat;
import java.util.List;
import shop.woowasap.order.domain.OrderProduct;

public class InvalidOrderProductException extends RuntimeException {

    public InvalidOrderProductException(List<OrderProduct> orderProducts) {
        super(MessageFormat.format("orderProduct는 \"{0}\" 는  0개 이상이여야 합니다.", orderProducts));
    }
}
