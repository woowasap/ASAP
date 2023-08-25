package shop.woowasap.order.repository.entity;

import java.io.Serializable;
import java.util.Objects;

public class OrderProductId implements Serializable {

    private OrderEntity orderEntity;
    private Long productId;

    private OrderProductId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderProductId that)) {
            return false;
        }
        return Objects.equals(orderEntity.getId(), that.orderEntity.getId()) && Objects.equals(productId,
                that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderEntity.getId(), productId);
    }
}
