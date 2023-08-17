package shop.woowasap.shop.repository.support;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import shop.woowasap.shop.domain.product.Product;

public class ProductFixture {

    public static final String NAME = "productName";
    public static final String DESCRIPTION = "productDescription";
    public static final String PRICE = "10000";
    public static final long QUANTITY = 10L;

    public static final LocalDateTime SALE_PAST_START_TIME = LocalDateTime.of(2022, 8, 5, 12, 30);
    public static final LocalDateTime SALE_PAST_END_TIME = LocalDateTime.of(2022, 8, 5, 14, 30);

    public static final LocalDateTime ON_SALE_START_TIME = LocalDateTime.of(2000, 12, 31, 23, 59);
    public static final LocalDateTime ON_SALE_END_TIME = LocalDateTime.of(2030, 12, 31, 23, 59);

    public static final LocalDateTime SALE_PRIOR_START_TIME = LocalDateTime.of(2030, 12, 1, 23, 59);
    public static final LocalDateTime SALE_PRIOR_END_TIME = LocalDateTime.of(2030, 12, 31, 23, 59);

    public static Product salePastProduct(final Long id) {
        return Product.builder()
            .id(id)
            .name(NAME)
            .description(DESCRIPTION)
            .price(PRICE)
            .quantity(QUANTITY)
            .startTime(SALE_PAST_START_TIME.toInstant(ZoneOffset.UTC))
            .endTime(SALE_PAST_END_TIME.toInstant(ZoneOffset.UTC))
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

    public static Product salePriorProduct(final Long id) {
        return Product.builder()
            .id(id)
            .name(NAME)
            .description(DESCRIPTION)
            .price(PRICE)
            .quantity(QUANTITY)
            .startTime(SALE_PRIOR_START_TIME.toInstant(ZoneOffset.UTC))
            .endTime(SALE_PRIOR_END_TIME.toInstant(ZoneOffset.UTC))
            .build();
    }
}
