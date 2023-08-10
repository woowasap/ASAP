package shop.woowasap.shop.repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import shop.woowasap.shop.app.product.Product;
import shop.woowasap.shop.app.spi.ProductRepository;
import shop.woowasap.shop.app.spi.response.ProductsPaginationResponse;

@Repository
public class MockProductRepository implements ProductRepository {

    public static final String NAME = "productName";
    public static final String DESCRIPTION = "productDescription";
    public static final String PRICE = "10000";
    public static final long QUANTITY = 10L;
    public static final LocalDateTime START_TIME = LocalDateTime.of(2023, 8, 5, 12, 30);
    public static final LocalDateTime END_TIME = LocalDateTime.of(2023, 8, 5, 14, 30);
    public static final LocalDateTime INFINITE_TIME = LocalDateTime.of(9999, 12, 31, 23, 59);

    @Override
    public Product persist(final Product product) {
        return product;
    }

    @Override
    public Optional<Product> findById(long productId) {
        if (productId == 123L) {
            return Optional.empty();
        }
        return Optional.of(Product.builder()
            .id(productId)
            .name(NAME)
            .description(DESCRIPTION)
            .price(PRICE)
            .quantity(QUANTITY)
            .startTime(START_TIME.toInstant(ZoneOffset.UTC))
            .endTime(END_TIME.toInstant(ZoneOffset.UTC)).build());
    }

    @Override
    public ProductsPaginationResponse findAllValidWithPagination(int page, int size) {
        Product product1 = Product.builder()
            .id(1L)
            .name(NAME)
            .price(PRICE)
            .quantity(QUANTITY)
            .startTime(INFINITE_TIME.toInstant(ZoneOffset.UTC))
            .endTime(INFINITE_TIME.toInstant(ZoneOffset.UTC))
            .build();

        Product product2 = Product.builder()
            .id(2L)
            .name(NAME)
            .price(PRICE)
            .quantity(QUANTITY)
            .startTime(INFINITE_TIME.toInstant(ZoneOffset.UTC))
            .endTime(INFINITE_TIME.toInstant(ZoneOffset.UTC))
            .build();

        return new ProductsPaginationResponse(List.of(product1, product2), 1, 1);
    }
}
