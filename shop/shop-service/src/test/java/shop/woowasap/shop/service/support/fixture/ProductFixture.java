package shop.woowasap.shop.service.support.fixture;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
import shop.woowasap.shop.app.product.Product;
import shop.woowasap.shop.app.api.request.RegisterProductRequest;
import shop.woowasap.shop.app.api.request.UpdateProductRequest;
import shop.woowasap.shop.app.api.response.ProductsResponse;

public class ProductFixture {

    public static final String NAME = "productName";
    public static final String DESCRIPTION = "productDescription";
    public static final String PRICE = "10000";
    public static final long QUANTITY = 10L;
    public static final LocalDateTime START_TIME = LocalDateTime.of(2023, 8, 5, 12, 30);
    public static final LocalDateTime END_TIME = LocalDateTime.of(2023, 8, 5, 14, 30);
    public static final LocalDateTime INFINITE_START_TIME = LocalDateTime.of(99_999, 12, 31, 23, 59);
    public static final LocalDateTime INFINITE_END_TIME = LocalDateTime.of(999_999, 12, 31, 23, 59);

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

    public static Product validProduct(final Long id) {
        return Product.builder()
            .id(id)
            .name(NAME)
            .description(DESCRIPTION)
            .price(PRICE)
            .quantity(QUANTITY)
            .startTime(INFINITE_START_TIME.toInstant(ZoneOffset.UTC))
            .endTime(INFINITE_END_TIME.toInstant(ZoneOffset.UTC))
            .build();
    }

    public static List<ProductsResponse.Product> productsOfProductsResponse(List<Product> products) {
        return products.stream()
            .map(ProductFixture::productOfProductsResponse)
            .collect(Collectors.toList());
    }

    private static ProductsResponse.Product productOfProductsResponse(Product product) {
        return new ProductsResponse.Product(
            product.getId(),
            product.getName().getValue(),
            product.getPrice().getValue().longValue(),
            LocalDateTime.ofInstant(product.getStartTime(), ZoneId.of("Asia/Seoul")),
            LocalDateTime.ofInstant(product.getEndTime(), ZoneId.of("Asia/Seoul"))
        );
    }

    public static RegisterProductRequest registerProductRequest() {
        return new RegisterProductRequest(NAME, DESCRIPTION, PRICE, QUANTITY, START_TIME, END_TIME);
    }

    public static UpdateProductRequest updateProductRequest() {
        return new UpdateProductRequest(NAME, DESCRIPTION, PRICE, QUANTITY, START_TIME, END_TIME);
    }
}
