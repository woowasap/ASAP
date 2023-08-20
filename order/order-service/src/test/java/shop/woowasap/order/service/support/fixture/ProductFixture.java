package shop.woowasap.order.service.support.fixture;

import java.time.Instant;
import shop.woowasap.shop.domain.product.Product;
import shop.woowasap.shop.domain.product.SaleTime;

public class ProductFixture {

    private ProductFixture() {
        throw new UnsupportedOperationException("Cannot invoke constructor \"DomainFixture()\"");
    }

    public static Product.ProductBuilder getDefaultBuilder() {
        final SaleTime saleTime = SaleTime.builder()
            .startTime(Instant.now().minusSeconds(100))
            .endTime(Instant.now().plusSeconds(100))
            .build();

        return Product.builder()
            .id(1L)
            .name("name")
            .description("description")
            .price("10000")
            .quantity(10L)
            .saleTime(saleTime);
    }
}
