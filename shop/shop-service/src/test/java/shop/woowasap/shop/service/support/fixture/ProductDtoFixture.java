package shop.woowasap.shop.service.support.fixture;

import java.time.LocalDateTime;
import java.time.ZoneId;
import shop.woowasap.shop.domain.in.product.response.ProductDetailsResponse;
import shop.woowasap.shop.domain.product.Product;

public final class ProductDtoFixture {

    private ProductDtoFixture() {
        throw new UnsupportedOperationException(
            "Cannot invoke constructor \"ProductDtoFixture()\"");
    }

    public static ProductDetailsResponse fromProduct(Product product) {
        return new ProductDetailsResponse(product.getId(), product.getName().getValue(),
            product.getDescription().getValue(), product.getPrice().getValue().toString(),
            product.getQuantity().getValue(),
            LocalDateTime.ofInstant(product.getSaleTime().getStartTime(), ZoneId.of("UTC")),
            LocalDateTime.ofInstant(product.getSaleTime().getEndTime(), ZoneId.of("UTC")));
    }

}
