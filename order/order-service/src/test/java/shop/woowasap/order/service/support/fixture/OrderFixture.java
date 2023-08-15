package shop.woowasap.order.service.support.fixture;

import java.util.List;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.OrderProduct;

public final class OrderFixture {

    private static final long DEFAULT_ID = 1L;
    private static final long DEFAULT_USER_ID = 1L;

    private OrderFixture() {
        throw new UnsupportedOperationException("Cannot invoke constructor \"OrderFixture()\"");
    }

    public static Order getDefault(final List<OrderProduct> orderProducts) {
        return Order.builder()
            .id(DEFAULT_ID)
            .userId(DEFAULT_USER_ID)
            .orderProducts(orderProducts)
            .build();
    }
}
