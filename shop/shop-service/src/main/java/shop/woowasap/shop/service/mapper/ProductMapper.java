package shop.woowasap.shop.service.mapper;

import java.time.ZoneOffset;
import shop.woowasap.core.id.api.IdGenerator;
import shop.woowasap.shop.domain.product.Product;
import shop.woowasap.shop.service.dto.RegisterProductRequest;

public final class ProductMapper {

    private ProductMapper() {
    }

    public static Product toDomain(final IdGenerator idGenerator, final RegisterProductRequest registerProductRequest) {
        return Product.builder()
            .id(idGenerator.generate())
            .name(registerProductRequest.name())
            .description(registerProductRequest.description())
            .price(registerProductRequest.price())
            .quantity(registerProductRequest.quantity())
            .startTime(registerProductRequest.startTime().toInstant(ZoneOffset.UTC))
            .endTime(registerProductRequest.endTime().toInstant(ZoneOffset.UTC))
            .build();
    }
}
