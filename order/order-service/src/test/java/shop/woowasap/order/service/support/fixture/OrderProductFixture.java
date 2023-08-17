package shop.woowasap.order.service.support.fixture;

import shop.woowasap.order.domain.OrderProduct;
import shop.woowasap.shop.domain.product.Product;

public final class OrderProductFixture {

    private OrderProductFixture() {
        throw new UnsupportedOperationException("Cannot invoke constructor \"OrderProductFixture()\"");
    }

    public static OrderProduct from(final Product product) {
        return OrderProduct.builder()
            .productId(product.getId())
            .name(product.getName().getValue().toString())
            .startTime(product.getStartTime())
            .endTime(product.getEndTime())
            .price(String.valueOf(product.getPrice().getValue()))
            .quantity(product.getQuantity().getValue())
            .build();
    }
}
