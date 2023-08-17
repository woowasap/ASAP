package shop.woowasap.order.repository.support;

import java.util.List;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.OrderProduct;

public class OrderFixture {

    private static final long DEFAULT_ORDER_ID = 1L;
    private static final long DEFAULT_USER_ID = 1L;

    private static final List<OrderProduct> DEFAULT_ORDER_PRODUCTS = List.of(
        OrderProductFixture.defaultBuilder().productId(1L).build(),
        OrderProductFixture.defaultBuilder().productId(2L).build(),
        OrderProductFixture.defaultBuilder().productId(3L).build()
    );

    private OrderFixture() {
        throw new UnsupportedOperationException("Cannot invoke constructor \"OrderFixture()\"");
    }

    public static Order.OrderBuilder defaultBuilder() {
        return Order.builder()
            .id(DEFAULT_ORDER_ID)
            .userId(DEFAULT_USER_ID)
            .orderProducts(DEFAULT_ORDER_PRODUCTS);
    }

}
