package shop.woowasap.order.domain.exception;

import java.text.MessageFormat;
import java.time.Instant;

public class InvalidProductSaleTimeException extends RuntimeException {

    public InvalidProductSaleTimeException(Instant startTime, Instant currentTime, Instant endTime) {
        super(MessageFormat.format(
                "상품을 구매할 수 있는시간은 startTime \"{0}\" <= currentTime \"{1}\" < endTime \"{2}\" 를 만족해야 합니다.", startTime,
                currentTime, endTime));
    }
}
