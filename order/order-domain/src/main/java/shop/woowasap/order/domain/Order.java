package shop.woowasap.order.domain;

import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import shop.woowasap.order.domain.exception.InvalidOrderProductException;

@Getter
public class Order {

    private final long id;
    private final long userId;
    private final List<OrderProduct> orderProducts;
    private final BigInteger totalPrice;
    private final Instant createdAt;
    private final OrderType orderType;

    @Builder
    private Order(final long id, final long userId, final List<OrderProduct> orderProducts,
        final Instant createdAt, final OrderType orderType) {
        validOrderProducts(orderProducts);
        this.id = id;
        this.userId = userId;
        this.orderProducts = orderProducts;
        this.totalPrice = calculatePrice();
        this.createdAt = createdAt == null ? Instant.now() : createdAt;
        this.orderType = orderType == null ? OrderType.PENDING : orderType;
    }

    public Order updateOrderType(final OrderType orderType) {
        return Order.builder()
            .id(this.id)
            .userId(this.userId)
            .orderProducts(this.orderProducts)
            .orderType(orderType)
            .createdAt(this.createdAt)
            .build();
    }

    private void validOrderProducts(final List<OrderProduct> orderProducts) {
        if (Objects.isNull(orderProducts) || orderProducts.isEmpty()) {
            throw new InvalidOrderProductException(orderProducts);
        }
    }

    private BigInteger calculatePrice() {
        return orderProducts.stream()
            .map(OrderProduct::getPrice)
            .reduce(BigInteger::add)
            .orElseThrow(IllegalStateException::new);
    }
}
