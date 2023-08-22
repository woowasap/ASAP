package shop.woowasap.shop.service.support.fixture;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
import shop.woowasap.shop.domain.in.product.request.RegisterProductRequest;
import shop.woowasap.shop.domain.in.product.request.UpdateProductRequest;
import shop.woowasap.shop.domain.in.product.response.ProductResponse;
import shop.woowasap.shop.domain.in.product.response.ProductsResponse;
import shop.woowasap.shop.domain.product.Product;
import shop.woowasap.shop.domain.product.SaleTime;

public class ProductFixture {

    public static final String NAME = "productName";
    public static final String DESCRIPTION = "productDescription";
    public static final String PRICE = "10000";
    public static final long QUANTITY = 10L;

    public static final int PAGE = 1;
    public static final int TOTAL_PAGE = 1;

    public static final LocalDateTime AFTER_SALE_START_TIME = LocalDateTime.of(2022, 8, 5, 12, 30);
    public static final LocalDateTime AFTER_SALE_END_TIME = LocalDateTime.of(2022, 8, 5, 14, 30);

    public static final LocalDateTime ON_SALE_START_TIME = LocalDateTime.of(2023, 8, 1, 0, 0);
    public static final LocalDateTime ON_SALE_END_TIME = LocalDateTime.of(2023, 8, 1, 3, 0);

    public static final LocalDateTime BEFORE_SALE_START_TIME = LocalDateTime.of(2023, 8, 2, 0, 0);
    public static final LocalDateTime BEFORE_SALE_END_TIME = LocalDateTime.of(2023, 8, 2, 1, 0);

    public static Product.ProductBuilder onSaleProductBuilder(final Long id) {
        final SaleTime saleTime = SaleTime.builder()
            .startTime(ON_SALE_START_TIME.toInstant(ZoneOffset.UTC))
            .endTime(ON_SALE_END_TIME.toInstant(ZoneOffset.UTC))
            .build();

        return Product.builder()
            .id(id)
            .name(NAME)
            .description(DESCRIPTION)
            .price(PRICE)
            .quantity(QUANTITY)
            .saleTime(saleTime);
    }

    public static Product.ProductBuilder beforeSaleProductBuilder(final Long id) {
        final SaleTime saleTime = SaleTime.builder()
            .startTime(BEFORE_SALE_START_TIME.toInstant(ZoneOffset.UTC))
            .endTime(BEFORE_SALE_END_TIME.toInstant(ZoneOffset.UTC))
            .build();

        return Product.builder()
            .id(id)
            .name(NAME)
            .description(DESCRIPTION)
            .price(PRICE)
            .quantity(QUANTITY)
            .saleTime(saleTime);
    }

    public static Product beforeSaleProduct(final Long id) {

        final SaleTime saleTime = SaleTime.builder()
            .startTime(BEFORE_SALE_START_TIME.toInstant(ZoneOffset.UTC))
            .endTime(BEFORE_SALE_END_TIME.toInstant(ZoneOffset.UTC))
            .build();

        return Product.builder()
            .id(id)
            .name(NAME)
            .description(DESCRIPTION)
            .price(PRICE)
            .quantity(QUANTITY)
            .saleTime(saleTime)
            .build();
    }

    public static Product onSaleProduct(final Long id) {

        final SaleTime saleTime = SaleTime.builder()
            .startTime(ON_SALE_START_TIME.toInstant(ZoneOffset.UTC))
            .endTime(ON_SALE_END_TIME.toInstant(ZoneOffset.UTC))
            .build();

        return Product.builder()
            .id(id)
            .name(NAME)
            .description(DESCRIPTION)
            .price(PRICE)
            .quantity(QUANTITY)
            .saleTime(saleTime)
            .build();
    }

    public static Product afterSaleProduct(final Long id) {

        final SaleTime saleTime = SaleTime.builder()
            .startTime(AFTER_SALE_START_TIME.toInstant(ZoneOffset.UTC))
            .endTime(AFTER_SALE_END_TIME.toInstant(ZoneOffset.UTC))
            .build();

        return Product.builder()
            .id(id)
            .name(NAME)
            .description(DESCRIPTION)
            .price(PRICE)
            .quantity(QUANTITY)
            .saleTime(saleTime)
            .build();
    }

    public static List<ProductResponse> productsOfProductsResponse(final List<Product> products) {
        return products.stream()
            .map(ProductFixture::productOfProductsResponse)
            .collect(Collectors.toList());
    }

    private static ProductResponse productOfProductsResponse(final Product product) {

        return new ProductResponse(
            product.getId(),
            product.getName().getValue(),
            product.getPrice().getValue().toString(),
            LocalDateTime.ofInstant(product.getSaleTime().getStartTime(), ZoneId.of("UTC")),
            LocalDateTime.ofInstant(product.getSaleTime().getEndTime(), ZoneId.of("UTC"))
        );
    }

    public static RegisterProductRequest registerProductRequest(final Product product) {
        return new RegisterProductRequest(
            product.getName().getValue(),
            product.getDescription().getValue(),
            product.getPrice().getValue().toString(),
            product.getQuantity().getValue(),
            LocalDateTime.ofInstant(product.getSaleTime().getStartTime(), ZoneId.of("UTC")),
            LocalDateTime.ofInstant(product.getSaleTime().getEndTime(), ZoneId.of("UTC"))
        );
    }

    public static UpdateProductRequest updateProductRequest(final Product product) {
        return new UpdateProductRequest(
            product.getName().getValue(),
            product.getDescription().getValue(),
            product.getPrice().getValue().toString(),
            product.getQuantity().getValue(),
            LocalDateTime.ofInstant(product.getSaleTime().getStartTime(), ZoneId.of("UTC")),
            LocalDateTime.ofInstant(product.getSaleTime().getEndTime(), ZoneId.of("UTC"))
        );
    }

    public static ProductsResponse productsResponse(final List<Product> products) {
        final List<ProductResponse> productResponses = products.stream()
            .map(product -> new ProductResponse(
                product.getId(),
                product.getName().getValue(),
                product.getPrice().getValue().toString(),
                LocalDateTime.ofInstant(product.getSaleTime().getStartTime(), ZoneId.of("UTC")),
                LocalDateTime.ofInstant(product.getSaleTime().getEndTime(), ZoneId.of("UTC"))
            )).toList();

        return new ProductsResponse(productResponses, PAGE, TOTAL_PAGE);
    }
}
