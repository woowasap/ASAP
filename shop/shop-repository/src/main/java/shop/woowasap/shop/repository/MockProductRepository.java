package shop.woowasap.shop.repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import shop.woowasap.shop.app.product.Product;
import shop.woowasap.shop.app.product.Product.ProductBuilder;
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
    public static final LocalDateTime INFINITE_START_TIME = LocalDateTime.of(2030, 12, 1, 23, 59);
    public static final LocalDateTime INFINITE_END_TIME = LocalDateTime.of(2030, 12, 31, 23, 59);
    private static final String OFFSET_ID = "+09:00";

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
            .startTime(START_TIME.toInstant(ZoneOffset.of(OFFSET_ID)))
            .endTime(END_TIME.toInstant(ZoneOffset.of(OFFSET_ID))).build());
    }

    @Override
    public Optional<Product> findByIdAndValidSaleTime(final long productId) {
        if (productId == 123L || productId == 122L) {
            return Optional.empty();
        }
        return Optional.of(Product.builder()
            .id(productId)
            .name(NAME)
            .description(DESCRIPTION)
            .price(PRICE)
            .quantity(QUANTITY)
            .startTime(START_TIME.toInstant(ZoneOffset.of(OFFSET_ID)))
            .endTime(END_TIME.toInstant(ZoneOffset.of(OFFSET_ID))).build());
    }

    @Override
    public ProductsPaginationResponse findAllValidWithPagination(int page, int size) {
        ProductBuilder productBuilder = Product.builder()
            .id(1L)
            .name(NAME)
            .price(PRICE)
            .description(DESCRIPTION)
            .quantity(QUANTITY)
            .startTime(INFINITE_START_TIME.toInstant(ZoneOffset.of(OFFSET_ID)))
            .endTime(INFINITE_END_TIME.toInstant(ZoneOffset.of(OFFSET_ID)));

        List<Product> products = List.of(productBuilder.id(1L).build(), productBuilder.id(2L).build());
        return new ProductsPaginationResponse(products, 1, 1);
    }

    @Override
    public ProductsPaginationResponse findAllWithPagination(final int page, final int size) {
        final List<Product> products = List.of(Product.builder()
            .id(1L)
            .name(NAME)
            .description(DESCRIPTION)
            .price(PRICE)
            .quantity(QUANTITY)
            .startTime(START_TIME.toInstant(ZoneOffset.of(OFFSET_ID)))
            .endTime(END_TIME.toInstant(ZoneOffset.of(OFFSET_ID))).build());
        return new ProductsPaginationResponse(products, page, 1);
    }
}
