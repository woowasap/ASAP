package shop.woowasap.shop.repository.support;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import shop.woowasap.shop.domain.product.Product;

public class ProductFixture {

    public static final String NAME = "productName";
    public static final String DESCRIPTION = "productDescription";
    public static final String PRICE = "10000";
    public static final long QUANTITY = 10L;

    public static final LocalDateTime AFTER_SALE_START_TIME = LocalDateTime.of(2000, 1, 1, 1, 0);
    public static final LocalDateTime AFTER_SALE_END_TIME = LocalDateTime.of(2000, 1, 1, 2, 0);

    public static final LocalDateTime ON_SALE_START_TIME = LocalDateTime.of(2022, 8, 18, 1, 0);
    public static final LocalDateTime ON_SALE_END_TIME = LocalDateTime.of(2030, 12, 31, 23, 59);

    public static final LocalDateTime BEFORE_SALE_START_TIME = LocalDateTime.of(2030, 12, 1, 23,
        59);
    public static final LocalDateTime BEFORE_SALE_END_TIME = LocalDateTime.of(2030, 12, 31, 23, 59);

    public static Product beforeSaleProduct(final Long id) {
        return Product.builder()
            .id(id)
            .name(NAME)
            .description(DESCRIPTION)
            .price(PRICE)
            .quantity(QUANTITY)
            .startTime(BEFORE_SALE_START_TIME.toInstant(ZoneOffset.UTC))
            .endTime(BEFORE_SALE_END_TIME.toInstant(ZoneOffset.UTC))
            .build();
    }

    public static Product onSaleProduct(final Long id) {
        return Product.builder()
            .id(id)
            .name(NAME)
            .description(DESCRIPTION)
            .price(PRICE)
            .quantity(QUANTITY)
            .startTime(ON_SALE_START_TIME.toInstant(ZoneOffset.UTC))
            .endTime(ON_SALE_END_TIME.toInstant(ZoneOffset.UTC))
            .build();
    }

    public static Product afterSaleProduct(final Long id) {
        return Product.builder()
            .id(id)
            .name(NAME)
            .description(DESCRIPTION)
            .price(PRICE)
            .quantity(QUANTITY)
            .startTime(AFTER_SALE_START_TIME.toInstant(ZoneOffset.UTC))
            .endTime(AFTER_SALE_END_TIME.toInstant(ZoneOffset.UTC))
            .build();
    }

    public static Product onSaleProduct(final Long id, final Long quantity) {
        return Product.builder()
            .id(id)
            .name(NAME)
            .description(DESCRIPTION)
            .price(PRICE)
            .quantity(quantity)
            .startTime(ON_SALE_START_TIME.toInstant(ZoneOffset.UTC))
            .endTime(ON_SALE_END_TIME.toInstant(ZoneOffset.UTC))
            .build();
    }
}
