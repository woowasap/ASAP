package shop.woowasap.shop.domain;

import java.time.LocalDateTime;
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
    private final Long price;
    private final Long quantity;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public Product update(Long id, UpdateProductRequest updateProductRequest) {
        return Product.builder()
            .id(id)
            .name(updateProductRequest.name())
            .description(updateProductRequest.description())
            .price(Long.parseLong(updateProductRequest.price()))
            .quantity((long) updateProductRequest.quantity())
            .startTime(updateProductRequest.startTime())
            .endTime(updateProductRequest.endTime())
            .build();
    }

}
