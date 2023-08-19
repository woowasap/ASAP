package shop.woowasap.shop.service.support.fixture;

import java.time.Instant;
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

    private static final Instant BEFORE_ALL_TIME = Instant.parse("2022-08-01T00:00:00.000Z");
    private static final Instant NOW_TIME = Instant.parse("2023-08-01T00:00:00.000Z");

    public static Product.ProductBuilder onSaleProductBuilder(final Long id) {
        return Product.builder()
            .id(id)
            .name(NAME)
            .description(DESCRIPTION)
            .price(PRICE)
            .quantity(QUANTITY)
            .startTime(ON_SALE_START_TIME.toInstant(ZoneOffset.UTC))
            .endTime(ON_SALE_END_TIME.toInstant(ZoneOffset.UTC));
    }

    public static Product.ProductBuilder beforeSaleProductBuilder(final Long id) {
        return Product.builder()
            .id(id)
            .name(NAME)
            .description(DESCRIPTION)
            .price(PRICE)
            .quantity(QUANTITY)
            .startTime(BEFORE_SALE_START_TIME.toInstant(ZoneOffset.UTC))
            .endTime(BEFORE_SALE_END_TIME.toInstant(ZoneOffset.UTC));
    }

    public static Product beforeSaleProduct(final Long id) {
        return Product.createProduct(
            id,
            NAME,
            DESCRIPTION,
            PRICE,
            QUANTITY,
            BEFORE_SALE_START_TIME.toInstant(ZoneOffset.UTC),
            BEFORE_SALE_END_TIME.toInstant(ZoneOffset.UTC),
            NOW_TIME
        );
    }

    public static Product onSaleProduct(final Long id) {
        return Product.createProduct(
            id,
            NAME,
            DESCRIPTION,
            PRICE,
            QUANTITY,
            ON_SALE_START_TIME.toInstant(ZoneOffset.UTC),
            ON_SALE_END_TIME.toInstant(ZoneOffset.UTC),
            BEFORE_ALL_TIME
        );
    }

    public static Product afterSaleProduct(final Long id) {
        return Product.createProduct(
            id,
            NAME,
            DESCRIPTION,
            PRICE,
            QUANTITY,
            AFTER_SALE_START_TIME.toInstant(ZoneOffset.UTC),
            AFTER_SALE_END_TIME.toInstant(ZoneOffset.UTC),
            BEFORE_ALL_TIME
        );
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
            LocalDateTime.ofInstant(product.getStartTime(), ZoneId.of("Asia/Seoul")),
            LocalDateTime.ofInstant(product.getEndTime(), ZoneId.of("Asia/Seoul"))
        );
    }

    public static RegisterProductRequest registerProductRequest(final Product product) {
        return new RegisterProductRequest(
            product.getName().getValue(),
            product.getDescription().getValue(),
            product.getPrice().getValue().toString(),
            product.getQuantity().getValue(),
            LocalDateTime.ofInstant(product.getStartTime(), ZoneId.of("Asia/Seoul")),
            LocalDateTime.ofInstant(product.getEndTime(), ZoneId.of("Asia/Seoul"))
        );
    }

    public static UpdateProductRequest updateProductRequest(final Product product) {
        return new UpdateProductRequest(
            product.getName().getValue(),
            product.getDescription().getValue(),
            product.getPrice().getValue().toString(),
            product.getQuantity().getValue(),
            LocalDateTime.ofInstant(product.getStartTime(), ZoneId.of("Asia/Seoul")),
            LocalDateTime.ofInstant(product.getEndTime(), ZoneId.of("Asia/Seoul"))
        );
    }

    public static ProductsResponse productsResponse(final List<Product> products) {
        final List<ProductResponse> productResponses = products.stream()
            .map(product -> new ProductResponse(
                product.getId(),
                product.getName().getValue(),
                product.getPrice().getValue().toString(),
                LocalDateTime.ofInstant(product.getStartTime(), ZoneId.of("Asia/Seoul")),
                LocalDateTime.ofInstant(product.getEndTime(), ZoneId.of("Asia/Seoul"))
            )).toList();

        return new ProductsResponse(productResponses, PAGE, TOTAL_PAGE);
    }
}
