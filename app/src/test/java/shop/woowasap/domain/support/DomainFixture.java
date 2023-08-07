package shop.woowasap.domain.support;

import java.math.BigInteger;
import java.time.Instant;
import shop.woowasap.shop.domain.Product;

public class DomainFixture {

    private DomainFixture() {
        throw new UnsupportedOperationException("Cannot invoke constructor \"DomainFixture()\"");
    }

    public static Product.ProductBuilder getDefaultBuilder() {
        return Product.builder()
            .id(1L)
            .name("name")
            .description("description")
            .price(new BigInteger("10000"))
            .quantity(1000L)
            .startTime(Instant.parse("2023-08-05T20:00:00.000Z"))
            .endTime(Instant.parse("2023-08-05T20:10:00.000Z"));
    }
}
