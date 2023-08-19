package shop.woowasap.shop.domain.product;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import shop.woowasap.shop.domain.exception.InvalidProductSaleTimeException;
import shop.woowasap.shop.domain.exception.ProductModificationPermissionException;

@Getter
@ToString
@EqualsAndHashCode
public final class Product {

    public static final int MIN_DIFF_NOW_AND_START_SECOND = 60 * 10;
    public static final int MIN_SALE_DURATION_HOUR = 1;
    public static final int MAX_SALE_DURATION_HOUR = 12;
    public static final int SECONDS_OF_HOUR = 60 * 60;

    private final Long id;
    private final Name name;
    private final Description description;
    private final Price price;
    private final Quantity quantity;
    private final Instant startTime;
    private final Instant endTime;

    @Builder
    private Product(
        final Long id,
        final String name,
        final String description,
        final String price,
        final Long quantity,
        final Instant startTime,
        final Instant endTime
    ) {
        this.id = id;
        this.name = new Name(name);
        this.description = new Description(description);
        this.price = new Price(price);
        this.quantity = new Quantity(quantity);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Product update(
        final String name,
        final String description,
        final String price,
        final long quantity,
        final LocalDateTime startTime,
        final LocalDateTime endTime,
        final Instant nowTime
    ) {
        validateUpdateTime(nowTime);

        return Product.builder()
            .id(id)
            .name(name)
            .description(description)
            .price(price)
            .quantity(quantity)
            .startTime(startTime.atZone(ZoneOffset.UTC).toInstant())
            .endTime(endTime.atZone(ZoneOffset.UTC).toInstant())
            .build();
    }

    public static Product createProduct(
        final Long id,
        final String name,
        final String description,
        final String price,
        final Long quantity,
        final Instant startTime,
        final Instant endTime,
        final Instant nowTime
    ) {
        validateTime(nowTime, startTime, endTime);

        return builder()
            .id(id)
            .name(name)
            .description(description)
            .price(price)
            .quantity(quantity)
            .startTime(startTime.atZone(ZoneOffset.UTC).toInstant())
            .endTime(endTime.atZone(ZoneOffset.UTC).toInstant())
            .build();
    }

    private static void validateTime(final Instant nowTime, final Instant startTime,
        final Instant endTime) {
        validateSaleTime(startTime, endTime);
        validateStartTime(nowTime, startTime);
        validateSaleDuration(startTime, endTime);
    }

    private static void validateSaleTime(final Instant startTime, final Instant endTime) {
        if (startTime.isAfter(endTime)) {
            throw new InvalidProductSaleTimeException("startTime 이 endTime 보다 앞설 수는 없습니다.");
        }
    }

    private static void validateStartTime(final Instant nowTime, final Instant startTime) {
        if (startTime.isBefore(nowTime.plusSeconds(MIN_DIFF_NOW_AND_START_SECOND))) {
            throw new InvalidProductSaleTimeException("startTime 은 현재 시간으로부터 10분 이후여야합니다");
        }
    }

    private static void validateSaleDuration(final Instant startTime, final Instant endTime) {
        final long saleTimeSeconds = Duration.between(startTime, endTime).getSeconds();
        if (saleTimeSeconds < SECONDS_OF_HOUR * MIN_SALE_DURATION_HOUR
            || saleTimeSeconds > SECONDS_OF_HOUR * MAX_SALE_DURATION_HOUR) {
            throw new InvalidProductSaleTimeException(
                MessageFormat.format("판매시간이 유효하지 않습니다. 최소 {0} 시간 / 최대 {1} 시간, 현재 판매 시간 : {2} 초",
                    MIN_SALE_DURATION_HOUR, MAX_SALE_DURATION_HOUR, saleTimeSeconds)
            );
        }
    }

    private void validateUpdateTime(final Instant nowTime) {

        if (isOnSale(nowTime)) {
            throw new ProductModificationPermissionException(
                MessageFormat.format("현재 판매 중인 Product 는 수정할 수 없습니다. productId : \"{0}\"", id)
            );
        }
    }

    private boolean isOnSale(final Instant nowTime) {
        return nowTime.isAfter(startTime) && nowTime.isBefore(endTime);
    }

    public boolean isEndTimeBefore(final Instant time) {
        return endTime.isBefore(time);
    }
}
