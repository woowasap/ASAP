package shop.woowasap.shop.domain;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public final class Product {

    private final Long id;
    private final String name;
    private final String description;
    private final BigInteger price;
    private final Long quantity;
    private final Instant startTime;
    private final Instant endTime;

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
            .price(new BigInteger(price))
            .quantity(quantity)
            .startTime(startTime.atZone(ZoneOffset.UTC).toInstant())
            .endTime(endTime.atZone(ZoneOffset.UTC).toInstant())
            .build();
    }

}
