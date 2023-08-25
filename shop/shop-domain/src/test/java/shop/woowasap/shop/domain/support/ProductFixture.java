package shop.woowasap.shop.domain.support;

import java.time.Instant;
import shop.woowasap.shop.domain.product.Product;
import shop.woowasap.shop.domain.product.SaleTime;

public class ProductFixture {

    public static final String NAME = "productName";
    public static final String DESCRIPTION = "productDescription";
    public static final String PRICE = "10000";
    public static final long QUANTITY = 10L;

    private ProductFixture() {
        throw new UnsupportedOperationException("Cannot invoke constructor \"DomainFixture()\"");
    }

    public static Product.ProductBuilder getDefaultBuilder() {

        final SaleTime saleTime = SaleTime.builder()
            .startTime(Instant.parse("2023-08-05T20:00:00.000Z"))
            .endTime(Instant.parse("2023-08-05T21:00:00.000Z"))
            .build();

        return Product.builder()
            .id(1L)
            .name(NAME)
            .description(DESCRIPTION)
            .price(PRICE)
            .quantity(QUANTITY)
            .saleTime(saleTime);
    }

    public static Product getProductWithSaleTime(
        final Instant startTime,
        final Instant endTime,
        final Instant nowTime
    ) {

        final SaleTime saleTime = SaleTime.builder()
            .startTime(startTime)
            .endTime(endTime)
            .nowTime(nowTime)
            .build();

        return Product.builder()
            .id(1L)
            .name(NAME)
            .description(DESCRIPTION)
            .price(PRICE)
            .quantity(QUANTITY)
            .saleTime(saleTime)
            .build();
    }
}
