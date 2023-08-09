package shop.woowasap.shop.domain.product;

import java.time.Instant;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import shop.woowasap.shop.domain.product.exception.InvalidProductSaleTimeException;

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

    private void validateProductSaleTime(final Instant startTime, final Instant endTime) {
        if (!startTime.isBefore(endTime)) {
            throw new InvalidProductSaleTimeException();
        }
    }
}
