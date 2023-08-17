package shop.woowasap.order.repository.support;

import java.time.Instant;
import shop.woowasap.order.domain.OrderProduct;

public final class OrderProductFixture {

    private static final long DEFAULT_PRODUCT_ID = 1L;
    private static final String DEFAULT_NAME = "default name";
    private static final long DEFAULT_QUANTITY = 1L;
    private static final String DEFAULT_PRICE = "1";

    private OrderProductFixture() {
        throw new UnsupportedOperationException(
            "Cannot invoke constructor \"OrderProductFixture()\"");
    }

    public static OrderProduct defaultProduct() {
        return defaultBuilder().build();
    }

    public static OrderProduct.OrderProductBuilder defaultBuilder() {
        Instant startTime = Instant.now().minusSeconds(100);
        Instant endTime = Instant.now().plusSeconds(100);
        return OrderProduct.builder()
            .productId(DEFAULT_PRODUCT_ID)
            .name(DEFAULT_NAME)
            .quantity(DEFAULT_QUANTITY)
            .price(DEFAULT_PRICE)
            .startTime(startTime)
            .endTime(endTime);
    }

}
