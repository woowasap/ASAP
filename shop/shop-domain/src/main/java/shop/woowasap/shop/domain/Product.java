package shop.woowasap.shop.domain;

import java.math.BigInteger;
import java.time.Instant;
import java.time.ZoneOffset;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import shop.woowasap.shop.dto.UpdateProductRequest;

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

    public Product update(Long id, UpdateProductRequest updateProductRequest) {
        return Product.builder()
            .id(id)
            .name(updateProductRequest.name())
            .description(updateProductRequest.description())
            .price(new BigInteger(updateProductRequest.price()))
            .quantity((long) updateProductRequest.quantity())
            .startTime(updateProductRequest.startTime().atZone(ZoneOffset.UTC).toInstant())
            .endTime(updateProductRequest.endTime().atZone(ZoneOffset.UTC).toInstant())
            .build();
    }

}
