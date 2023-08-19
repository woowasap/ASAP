package shop.woowasap.shop.domain.product;

import java.text.MessageFormat;
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
        validateUpdateTime();
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

    private void validateUpdateTime() {
        if (isOnSale()) {
            throw new ProductModificationPermissionException(
                MessageFormat.format("현재 판매 중인 Product 는 수정할 수 없습니다. productId : \"{0}\"", id)
            );
        }
    }

    private boolean isOnSale() {
        final Instant now = Instant.now();
        return now.isAfter(startTime) && now.isBefore(endTime);
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
