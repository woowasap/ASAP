package shop.woowasap.order.domain.support.fixture;

import java.time.Instant;
import shop.woowasap.order.domain.OrderProduct;

public final class OrderProductFixture {

    private static final long DEFAULT_PRODUCT_ID = 1L;
    private static final long DEFAULT_QUANTITY = 1L;
    private static final String DEFAULT_PRICE = "1";

    private OrderProductFixture() {
        throw new UnsupportedOperationException("Cannot invoke constructor \"OrderProductFixture()\"");
    }

    public static OrderProduct defaultProduct() {
        return defaultBuilder().build();
    }

    public static OrderProduct.OrderProductBuilder defaultBuilder() {
        Instant currentTime = Instant.now();
        return OrderProduct.builder()
                .productId(DEFAULT_PRODUCT_ID)
                .quantity(DEFAULT_QUANTITY)
                .price(DEFAULT_PRICE)
                .startTime(currentTime)
                .endTime(currentTime);
    }

}
