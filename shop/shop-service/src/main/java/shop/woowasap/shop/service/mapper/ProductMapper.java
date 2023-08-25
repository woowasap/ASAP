package shop.woowasap.shop.service.mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import shop.woowasap.core.id.api.IdGenerator;
import shop.woowasap.shop.domain.in.product.request.RegisterProductRequest;
import shop.woowasap.shop.domain.in.product.response.ProductDetailsResponse;
import shop.woowasap.shop.domain.in.product.response.ProductResponse;
import shop.woowasap.shop.domain.in.product.response.ProductsResponse;
import shop.woowasap.shop.domain.in.product.response.ProductsResponseV2;
import shop.woowasap.shop.domain.out.response.ProductsPaginationResponse;
import shop.woowasap.shop.domain.out.response.ProductsPaginationResponseV2;
import shop.woowasap.shop.domain.product.Product;
import shop.woowasap.shop.domain.product.SaleTime;

public final class ProductMapper {

    private ProductMapper() {
    }

    public static Product toDomain(final IdGenerator idGenerator,
        final RegisterProductRequest registerProductRequest, final String offsetId,
        final Instant nowTime) {
        return Product.builder()
            .id(idGenerator.generate())
            .name(registerProductRequest.name())
            .description(registerProductRequest.description())
            .price(registerProductRequest.price())
            .quantity(registerProductRequest.quantity())
            .saleTime(SaleTime.builder()
                .startTime(registerProductRequest.startTime().toInstant(ZoneOffset.of(offsetId)))
                .endTime(registerProductRequest.endTime().toInstant(ZoneOffset.of(offsetId)))
                .nowTime(nowTime)
                .build())
            .build();
    }

    public static ProductDetailsResponse toProductResponse(final Product product,
        final ZoneId zoneId) {
        return new ProductDetailsResponse(product.getId(), product.getName().getValue(),
            product.getDescription().getValue(), product.getPrice().getValue().toString(),
            product.getQuantity().getValue(),
            LocalDateTime.ofInstant(product.getSaleTime().getStartTime(), zoneId),
            LocalDateTime.ofInstant(product.getSaleTime().getEndTime(), zoneId));
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
                LocalDateTime.ofInstant(product.getSaleTime().getStartTime(), zoneId),
                LocalDateTime.ofInstant(product.getSaleTime().getEndTime(), zoneId)
            )).toList();

        return new ProductsResponse(products, paginationResponse.page(),
            paginationResponse.totalPage());
    }

    public static ProductsResponseV2 toProductsResponseV2(
        final ProductsPaginationResponseV2 paginationResponse,
        final ZoneId zoneId
    ) {
        final List<ProductResponse> products = paginationResponse.products().stream()
            .map(product -> new ProductResponse(
                product.getId(),
                product.getName().getValue(),
                product.getPrice().getValue().toString(),
                LocalDateTime.ofInstant(product.getSaleTime().getStartTime(), zoneId),
                LocalDateTime.ofInstant(product.getSaleTime().getEndTime(), zoneId)
            )).toList();

        return new ProductsResponseV2(products, paginationResponse.hasNext());
    }
}
