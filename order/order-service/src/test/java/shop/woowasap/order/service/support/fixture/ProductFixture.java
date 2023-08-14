package shop.woowasap.order.service.support.fixture;

import java.time.Instant;
import shop.woowasap.shop.app.product.Product;

public class ProductFixture {

    private ProductFixture() {
        throw new UnsupportedOperationException("Cannot invoke constructor \"DomainFixture()\"");
    }

    public static Product.ProductBuilder getDefaultBuilder() {
        final Instant startTime = Instant.now().minusSeconds(100);
        final Instant endTime = Instant.now().plusSeconds(100);
        return Product.builder()
                .id(1L)
                .name("name")
                .description("description")
                .price("10000")
                .quantity(1000L)
                .startTime(startTime)
                .endTime(endTime);
    }
}
