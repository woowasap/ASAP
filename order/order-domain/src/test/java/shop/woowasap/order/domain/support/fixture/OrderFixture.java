package shop.woowasap.order.domain.support.fixture;

import java.util.List;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.OrderProduct;

public class OrderFixture {

    private static final long DEFAULT_ORDER_ID = 1L;

    private static final List<OrderProduct> DEFAULT_ORDER_PRODUCTS = List.of(
            OrderProductFixture.defaultProduct(),
            OrderProductFixture.defaultProduct(),
            OrderProductFixture.defaultProduct()
    );

    private OrderFixture() {
        throw new UnsupportedOperationException("Cannot invoke constructor \"OrderFixture()\"");
    }

    public static Order.OrderBuilder defaultBuilder() {
        return Order.builder()
                .id(DEFAULT_ORDER_ID)
                .orderProducts(DEFAULT_ORDER_PRODUCTS);
    }

}
