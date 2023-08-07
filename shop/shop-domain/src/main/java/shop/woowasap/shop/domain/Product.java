package shop.woowasap.shop.domain;

import java.math.BigInteger;
import java.time.Instant;
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
    private final long quantity;
    private final Instant startTime;
    private final Instant endTime;

}
