package shop.woowasap.shop.domain.support;

import java.time.Instant;
import shop.woowasap.shop.domain.product.Product;

public class ProductFixture {

    public static final String NAME = "productName";
    public static final String DESCRIPTION = "productDescription";
    public static final String PRICE = "10000";
    public static final long QUANTITY = 10L;

    private ProductFixture() {
        throw new UnsupportedOperationException("Cannot invoke constructor \"DomainFixture()\"");
    }

    public static Product.ProductBuilder getDefaultBuilder() {
        return Product.builder()
            .id(1L)
            .name(NAME)
            .description(DESCRIPTION)
            .price(PRICE)
            .quantity(QUANTITY)
            .startTime(Instant.parse("2023-08-05T20:00:00.000Z"))
            .endTime(Instant.parse("2023-08-05T21:00:00.000Z"));
    }

    public static Product getProductWithSaleTime(
        final Instant startTime,
        final Instant endTime,
        final Instant nowTime
    ) {
        return Product.builder()
            .id(1L)
            .name(NAME)
            .description(DESCRIPTION)
            .price(PRICE)
            .quantity(QUANTITY)
            .startTime(startTime)
            .endTime(endTime)
            .nowTime(nowTime)
            .build();
    }
}
