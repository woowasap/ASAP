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
import shop.woowasap.shop.domain.in.product.response.ProductsAdminResponse;
import shop.woowasap.shop.domain.in.product.response.ProductsResponse;
import shop.woowasap.shop.domain.out.response.ProductsPaginationAdminResponse;
import shop.woowasap.shop.domain.out.response.ProductsPaginationResponse;
import shop.woowasap.shop.domain.product.Product;
import shop.woowasap.shop.domain.product.SaleTime;

public final class ProductMapper {

    private static final ZoneId ZONE_ID = ZoneId.of("UTC");

    private ProductMapper() {
    }

    public static Product toDomain(final IdGenerator idGenerator,
        final RegisterProductRequest registerProductRequest, final Instant nowTime) {
        return Product.builder()
            .id(idGenerator.generate())
            .name(registerProductRequest.name())
            .description(registerProductRequest.description())
            .price(registerProductRequest.price())
            .quantity(registerProductRequest.quantity())
            .saleTime(SaleTime.builder()
                .startTime(registerProductRequest.startTime().toInstant(ZoneOffset.UTC))
                .endTime(registerProductRequest.endTime().toInstant(ZoneOffset.UTC))
                .nowTime(nowTime)
                .build())
            .build();
    }

    public static ProductDetailsResponse toProductResponse(final Product product) {
        return new ProductDetailsResponse(product.getId(), product.getName().getValue(),
            product.getDescription().getValue(), product.getPrice().getValue().toString(),
            product.getQuantity().getValue(),
            LocalDateTime.ofInstant(product.getSaleTime().getStartTime(), ZONE_ID),
            LocalDateTime.ofInstant(product.getSaleTime().getEndTime(), ZONE_ID));
    }

    public static ProductsAdminResponse toProductsAdminResponse(
        final ProductsPaginationAdminResponse paginationResponse) {
        final List<ProductResponse> products = paginationResponse.products().stream()
            .map(product -> new ProductResponse(
                product.getId(),
                product.getName().getValue(),
                product.getPrice().getValue().toString(),
                LocalDateTime.ofInstant(product.getSaleTime().getStartTime(), ZONE_ID),
                LocalDateTime.ofInstant(product.getSaleTime().getEndTime(), ZONE_ID)
            )).toList();

        return new ProductsAdminResponse(products, paginationResponse.page(),
            paginationResponse.totalPage());
    }

    public static ProductsResponse toProductsResponse(
        final ProductsPaginationResponse paginationResponse) {
        final List<ProductResponse> products = paginationResponse.products().stream()
            .map(product -> new ProductResponse(
                product.getId(),
                product.getName().getValue(),
                product.getPrice().getValue().toString(),
                LocalDateTime.ofInstant(product.getSaleTime().getStartTime(), ZONE_ID),
                LocalDateTime.ofInstant(product.getSaleTime().getEndTime(), ZONE_ID)
            )).toList();

        return new ProductsResponse(products, paginationResponse.hasNext());
    }
}
