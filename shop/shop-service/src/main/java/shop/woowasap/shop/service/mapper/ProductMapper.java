package shop.woowasap.shop.service.mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import shop.woowasap.core.id.api.IdGenerator;
import shop.woowasap.shop.domain.api.product.response.ProductDetailsResponse;
import shop.woowasap.shop.domain.api.product.response.ProductResponse;
import shop.woowasap.shop.domain.api.product.response.ProductsResponse;
import shop.woowasap.shop.domain.product.Product;
import shop.woowasap.shop.domain.api.product.request.RegisterProductRequest;
import shop.woowasap.shop.domain.spi.response.ProductsPaginationResponse;

public final class ProductMapper {

    private ProductMapper() {
    }

    public static Product toDomain(final IdGenerator idGenerator,
        final RegisterProductRequest registerProductRequest, final String offsetId) {
        return Product.builder()
            .id(idGenerator.generate())
            .name(registerProductRequest.name())
            .description(registerProductRequest.description())
            .price(registerProductRequest.price())
            .quantity(registerProductRequest.quantity())
            .startTime(registerProductRequest.startTime().toInstant(ZoneOffset.of(offsetId)))
            .endTime(registerProductRequest.endTime().toInstant(ZoneOffset.of(offsetId)))
            .build();
    }

    public static ProductDetailsResponse toProductResponse(final Product product, final ZoneId zoneId) {
        return new ProductDetailsResponse(product.getId(), product.getName().getValue(),
            product.getDescription().getValue(), product.getPrice().getValue().toString(),
            product.getQuantity().getValue(),
            LocalDateTime.ofInstant(product.getStartTime(), zoneId),
            LocalDateTime.ofInstant(product.getEndTime(), zoneId));
    }

    public static ProductsResponse toProductsResponse(
        final ProductsPaginationResponse paginationResponse,
        final ZoneId zoneId
    ) {
        final List<ProductResponse> products = paginationResponse.products().stream()
            .map(product -> new ProductResponse(
                product.getId(),
                product.getName().getValue(),
                product.getPrice().getValue().toString(),
                LocalDateTime.ofInstant(product.getStartTime(), zoneId),
                LocalDateTime.ofInstant(product.getEndTime(), zoneId)
            )).toList();

        return new ProductsResponse(products, paginationResponse.page(),
            paginationResponse.totalPage());
    }
}
