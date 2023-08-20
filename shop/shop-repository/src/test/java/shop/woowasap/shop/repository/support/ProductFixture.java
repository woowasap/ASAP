package shop.woowasap.shop.repository.support;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import shop.woowasap.shop.domain.product.Product;
import shop.woowasap.shop.domain.product.SaleTime;

public class ProductFixture {

    public static final String NAME = "productName";
    public static final String DESCRIPTION = "productDescription";
    public static final String PRICE = "10000";
    public static final long QUANTITY = 10L;

    public static final LocalDateTime AFTER_SALE_START_TIME = LocalDateTime.of(2022, 8, 5, 12, 30);
    public static final LocalDateTime AFTER_SALE_END_TIME = LocalDateTime.of(2022, 8, 5, 14, 30);

    public static final LocalDateTime ON_SALE_START_TIME = LocalDateTime.of(2023, 8, 1, 0, 0);
    public static final LocalDateTime ON_SALE_END_TIME = LocalDateTime.of(2023, 8, 1, 3, 0);

    public static final LocalDateTime BEFORE_SALE_START_TIME = LocalDateTime.of(2023, 8, 2, 0, 0);
    public static final LocalDateTime BEFORE_SALE_END_TIME = LocalDateTime.of(2023, 8, 2, 1, 0);

    private static final Instant BEFORE_ALL_TIME = Instant.parse("2022-08-01T00:00:00.000Z");

    public static Product beforeSaleProduct(final Long id) {

        final SaleTime saleTime = SaleTime.builder()
            .startTime(BEFORE_SALE_START_TIME.toInstant(ZoneOffset.UTC))
            .endTime(BEFORE_SALE_END_TIME.toInstant(ZoneOffset.UTC))
            .nowTime(BEFORE_ALL_TIME)
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
            .nowTime(BEFORE_ALL_TIME)
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

    public static Product onSaleProduct(final Long id, final long quantity) {
        final SaleTime saleTime = SaleTime.builder()
            .startTime(ON_SALE_START_TIME.toInstant(ZoneOffset.UTC))
            .endTime(ON_SALE_END_TIME.toInstant(ZoneOffset.UTC))
            .nowTime(BEFORE_ALL_TIME)
            .build();

        return Product.builder()
            .id(id)
            .name(NAME)
            .description(DESCRIPTION)
            .price(PRICE)
            .quantity(quantity)
            .saleTime(saleTime)
            .build();
    }

    public static Product afterSaleProduct(final Long id) {
        final SaleTime saleTime = SaleTime.builder()
            .startTime(AFTER_SALE_START_TIME.toInstant(ZoneOffset.UTC))
            .endTime(AFTER_SALE_END_TIME.toInstant(ZoneOffset.UTC))
            .nowTime(BEFORE_ALL_TIME)
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
}
