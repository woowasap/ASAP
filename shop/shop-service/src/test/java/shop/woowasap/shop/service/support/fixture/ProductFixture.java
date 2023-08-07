package shop.woowasap.shop.service.support.fixture;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import shop.woowasap.shop.domain.Product;
import shop.woowasap.shop.service.dto.ProductResponse;

public final class ProductFixture {

    private ProductFixture() {
        throw new UnsupportedOperationException("Cannot invoke constructor \"ProductFixture()\"");
    }

    public static Product.ProductBuilder defaultBuilder() {
        return Product.builder()
            .id(1L)
            .name("default-product-name")
            .description("default-product-description")
            .price(BigInteger.ONE)
            .quantity(1000L)
            .startTime(Instant.now())
            .endTime(Instant.now().plus(1, ChronoUnit.HOURS));
    }

    public static ProductResponse toProductResponse(final Product product, final String zoneId) {
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(),
            product.getPrice().toString(), product.getQuantity(),
            LocalDateTime.ofInstant(product.getStartTime(), ZoneId.of(zoneId)),
            LocalDateTime.ofInstant(product.getEndTime(), ZoneId.of(zoneId)));
    }
}
