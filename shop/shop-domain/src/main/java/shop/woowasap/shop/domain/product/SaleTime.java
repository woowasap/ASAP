package shop.woowasap.shop.domain.product;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import shop.woowasap.shop.domain.exception.InvalidProductSaleDurationException;
import shop.woowasap.shop.domain.exception.InvalidProductSaleTimeException;
import shop.woowasap.shop.domain.exception.InvalidProductStartTimeException;

@Getter
@ToString
@EqualsAndHashCode
public class SaleTime {

    public static final int MIN_DIFF_NOW_AND_START_MINUTE = 10;
    public static final int MIN_SALE_DURATION_HOUR = 1;
    public static final int MAX_SALE_DURATION_HOUR = 24 * 10;
    public static final int SECONDS_OF_HOUR = 60 * 60;
    public static final int SECONDS_OF_MINUTE = 60;

    private final Instant startTime;
    private final Instant endTime;

    @Builder
    private SaleTime(
        final Instant startTime,
        final Instant endTime,
        final Instant nowTime
    ) {
        if (!Objects.isNull(nowTime)) {
            validateTime(nowTime, startTime, endTime);
        }
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private void validateTime(final Instant nowTime, final Instant startTime,
        final Instant endTime) {
        validateSaleTime(startTime, endTime);
        validateStartTime(nowTime, startTime);
        validateSaleDuration(startTime, endTime);
    }

    private void validateSaleTime(final Instant startTime, final Instant endTime) {
        if (startTime.isAfter(endTime)) {
            throw new InvalidProductSaleTimeException("startTime 이 endTime 보다 앞설 수는 없습니다.");
        }
    }

    private void validateStartTime(final Instant nowTime, final Instant startTime) {
        if (startTime.isBefore(
            nowTime.plusSeconds(SECONDS_OF_MINUTE * MIN_DIFF_NOW_AND_START_MINUTE))) {
            throw new InvalidProductStartTimeException(
                MessageFormat.format("startTime 은 현재 시간으로부터 \"{0}\"분 이후여야합니다.",
                    MIN_DIFF_NOW_AND_START_MINUTE));
        }
    }

    private void validateSaleDuration(final Instant startTime, final Instant endTime) {
        final long saleTimeSeconds = Duration.between(startTime, endTime).getSeconds();
        if (saleTimeSeconds < SECONDS_OF_HOUR * MIN_SALE_DURATION_HOUR
            || saleTimeSeconds > SECONDS_OF_HOUR * MAX_SALE_DURATION_HOUR) {
            throw new InvalidProductSaleDurationException(
                MessageFormat.format("판매 시간은 \"{0}\"시간과 \"{1}\"시간 사이어야합니다.", MIN_SALE_DURATION_HOUR,
                    MAX_SALE_DURATION_HOUR)
            );
        }
    }

    public boolean isOnSale(final Instant nowTime) {
        return nowTime.isAfter(startTime) && nowTime.isBefore(endTime);
    }

    public boolean isEndTimeBefore(final Instant time) {
        return endTime.isBefore(time);
    }

}
