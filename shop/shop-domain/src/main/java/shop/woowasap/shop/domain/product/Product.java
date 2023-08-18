package shop.woowasap.shop.domain.product;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import shop.woowasap.shop.domain.exception.InvalidProductSaleTimeException;

@Getter
@ToString
@EqualsAndHashCode
public final class Product {

    private final Long id;
    private final Name name;
    private final Description description;
    private final Price price;
    private final Quantity quantity;
    private final Instant startTime;
    private final Instant endTime;

    @Builder
    private Product(final Long id, final String name, final String description,
        final String price, final Long quantity, final Instant startTime,
        final Instant endTime) {
        validateProductSaleTime(startTime, endTime);
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
        final LocalDateTime endTime
    ) {
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

    private void validateProductSaleTime(final Instant startTime, final Instant endTime) {
        if (!startTime.isBefore(endTime)) {
            throw new InvalidProductSaleTimeException();
        }
    }

    public boolean isEndTimeBefore(final Instant time) {
        return endTime.isBefore(time);
    }
}
