package shop.woowasap.order.service.support.fixture;

import java.math.BigInteger;
import shop.woowasap.order.domain.OrderProduct;
import shop.woowasap.shop.domain.product.Product;

public final class OrderProductFixture {

    private OrderProductFixture() {
        throw new UnsupportedOperationException(
            "Cannot invoke constructor \"OrderProductFixture()\"");
    }

    public static OrderProduct from(final Product product) {
        final String price = String.valueOf(product.getPrice().getValue()
            .multiply(BigInteger.valueOf(product.getQuantity().getValue())));

        return OrderProduct.builder()
            .productId(product.getId())
            .name(product.getName().getValue())
            .startTime(product.getStartTime())
            .endTime(product.getEndTime())
            .price(price)
            .quantity(product.getQuantity().getValue())
            .build();
    }
}
