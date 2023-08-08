package shop.woowasap.domain.support;

import java.time.LocalDateTime;
import shop.woowasap.shop.dto.UpdateProductRequest;

public class ProductFixture {

    public static final String NAME = "productName";
    public static final String DESCRIPTION = "productDescription";
    public static final String PRICE = "10000";
    public static final int QUANTITY = 10;
    public static final LocalDateTime START_TIME = LocalDateTime.of(2023, 8, 5, 12, 30);
    public static final LocalDateTime END_TIME = LocalDateTime.of(2023, 8, 5, 14, 30);

    public static UpdateProductRequest updateProductRequest() {
        return new UpdateProductRequest(NAME, DESCRIPTION, PRICE, QUANTITY, START_TIME, END_TIME);
    }
}
