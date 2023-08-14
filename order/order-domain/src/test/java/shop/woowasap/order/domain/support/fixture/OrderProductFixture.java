package shop.woowasap.order.domain.support.fixture;

import shop.woowasap.order.domain.OrderProduct;

public final class OrderProductFixture {

    private static final long DEFAULT_PRODUCT_ID = 1L;
    private static final int DEFAULT_QUANTITY = 1;
    private static final String DEFAULT_PRICE = "1";

    private OrderProductFixture() {
        throw new UnsupportedOperationException("Cannot invoke constructor \"OrderProductFixture()\"");
    }

    public static OrderProduct.OrderProductBuilder defaultBuilder() {
        return OrderProduct.builder()
                .productId(DEFAULT_PRODUCT_ID)
                .quantity(DEFAULT_QUANTITY)
                .price(DEFAULT_PRICE);
    }

}
