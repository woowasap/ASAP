package shop.woowasap.shop.domain.product;

import java.time.Instant;

public class DomainFixture {

    static class ProductBuilder {

        static Product.ProductBuilder getDefaultBuilder() {
            return Product.builder()
                .name("name")
                .description("description")
                .price("10000")
                .quantity(1000L)
                .startTime(Instant.parse("2023-08-05T20:00:00.000Z"))
                .endTime(Instant.parse("2023-08-05T20:10:00.000Z"));
        }
    }
}
