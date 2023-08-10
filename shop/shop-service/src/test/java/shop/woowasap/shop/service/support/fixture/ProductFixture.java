package shop.woowasap.shop.service.support.fixture;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import shop.woowasap.shop.app.api.response.ProductsResponse;
import shop.woowasap.shop.app.api.response.ProductsResponse.ProductResponse;
import shop.woowasap.shop.app.product.Product;
import shop.woowasap.shop.app.api.request.RegisterProductRequest;
import shop.woowasap.shop.app.api.request.UpdateProductRequest;

public class ProductFixture {

    public static final String NAME = "productName";
    public static final String DESCRIPTION = "productDescription";
    public static final String PRICE = "10000";
    public static final long QUANTITY = 10L;
    public static final LocalDateTime START_TIME = LocalDateTime.of(2023, 8, 5, 12, 30);
    public static final LocalDateTime END_TIME = LocalDateTime.of(2023, 8, 5, 14, 30);
    public static final int PAGE = 1;
    public static final int TOTAL_PAGE = 1;

    public static Product.ProductBuilder productBuilder(final Long id) {
        return Product.builder()
            .id(id)
            .name(NAME)
            .description(DESCRIPTION)
            .price(PRICE)
            .quantity(QUANTITY)
            .startTime(START_TIME.toInstant(ZoneOffset.UTC))
            .endTime(END_TIME.toInstant(ZoneOffset.UTC));
    }

    public static RegisterProductRequest registerProductRequest() {
        return new RegisterProductRequest(NAME, DESCRIPTION, PRICE, QUANTITY, START_TIME, END_TIME);
    }

    public static UpdateProductRequest updateProductRequest() {
        return new UpdateProductRequest(NAME, DESCRIPTION, PRICE, QUANTITY, START_TIME, END_TIME);
    }

    public static ProductsResponse productsResponse(final List<Product> products) {
        final List<ProductResponse> productResponses = products.stream()
            .map(product -> new ProductResponse(
                product.getId(),
                product.getName().getValue(),
                product.getPrice().getValue().toString(),
                LocalDateTime.ofInstant(product.getStartTime(), ZoneId.of("Asia/Seoul")),
                LocalDateTime.ofInstant(product.getEndTime(), ZoneId.of("Asia/Seoul"))
            )).toList();

        return new ProductsResponse(productResponses, PAGE, TOTAL_PAGE);
    }
}
