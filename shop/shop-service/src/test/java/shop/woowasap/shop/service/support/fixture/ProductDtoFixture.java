package shop.woowasap.shop.service.support.fixture;

import java.time.LocalDateTime;
import java.time.ZoneId;
import shop.woowasap.shop.app.api.response.ProductResponse;
import shop.woowasap.shop.app.product.Product;

public final class ProductDtoFixture {

    private ProductDtoFixture() {
        throw new UnsupportedOperationException(
            "Cannot invoke constructor \"ProductDtoFixture()\"");
    }

    public static ProductResponse fromProduct(Product product) {
        return new ProductResponse(product.getId(), product.getName().getValue(),
            product.getDescription().getValue(), product.getPrice().getValue().toString(),
            product.getQuantity().getValue(),
            LocalDateTime.ofInstant(product.getStartTime(), ZoneId.of("Asia/Seoul")),
            LocalDateTime.ofInstant(product.getEndTime(), ZoneId.of("Asia/Seoul")));
    }

}
