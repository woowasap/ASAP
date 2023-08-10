package shop.woowasap.shop.service.mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import shop.woowasap.core.id.api.IdGenerator;
import shop.woowasap.shop.app.api.response.ProductResponse;
import shop.woowasap.shop.app.product.Product;
import shop.woowasap.shop.app.api.request.RegisterProductRequest;

public final class ProductMapper {

    private ProductMapper() {
    }

    public static Product toDomain(final IdGenerator idGenerator,
        final RegisterProductRequest registerProductRequest) {
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

    public static ProductResponse toProductResponse(final Product product, final ZoneId zoneId) {
        return new ProductResponse(product.getId(), product.getName().getValue(),
            product.getDescription().getValue(), product.getPrice().getValue().toString(),
            product.getQuantity().getValue(),
            LocalDateTime.ofInstant(product.getStartTime(), zoneId),
            LocalDateTime.ofInstant(product.getEndTime(), zoneId));
    }
}
