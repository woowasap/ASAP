package shop.woowasap.order.service.mapper;

import java.util.List;
import shop.woowasap.core.id.api.IdGenerator;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.OrderProduct;
import shop.woowasap.shop.app.product.Product;

public final class OrderMapper {

    private OrderMapper() {
        throw new UnsupportedOperationException("Cannot invoke constructor \"OrderMapper()\"");
    }

    public static Order toDomain(final IdGenerator idGenerator, final long userId, final Product product) {
        return Order.builder()
                .id(idGenerator.generate())
                .userId(userId)
                .orderProducts(List.of(OrderProduct
                        .builder()
                        .productId(product.getId())
                        .price(product.getPrice().getValue().toString())
                        .quantity(product.getQuantity().getValue())
                        .startTime(product.getStartTime())
                        .endTime(product.getEndTime())
                        .build()))
                .build();
    }

}
