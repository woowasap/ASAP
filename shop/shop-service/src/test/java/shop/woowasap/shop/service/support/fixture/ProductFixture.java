package shop.woowasap.shop.service.support.fixture;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import shop.woowasap.shop.domain.product.Product;
import shop.woowasap.shop.service.dto.RegisterProductRequest;

public class ProductFixture {

    public static final String NAME = "productName";
    public static final String DESCRIPTION = "productDescription";
    public static final String PRICE = "10000";
    public static final long QUANTITY = 10L;
    public static final LocalDateTime START_TIME = LocalDateTime.of(2023, 8, 5, 12, 30);
    public static final LocalDateTime END_TIME = LocalDateTime.of(2023, 8, 5, 14, 30);

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

}
