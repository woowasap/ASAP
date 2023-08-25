package shop.woowasap.order.repository.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.OrderType;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "orders")
public class OrderEntity extends BaseEntity {

    @Id
    @Column(name = "order_id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "orderEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProductEntity> orderProducts = new ArrayList<>();

    @Column(name = "total_price", nullable = false)
    private String totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", length = 10, nullable = false)
    private OrderEntityType orderType;

    public OrderEntity(final Order order) {
        this.id = order.getId();
        this.userId = order.getUserId();
        this.orderProducts = order.getOrderProducts()
            .stream()
            .map(orderProduct -> new OrderProductEntity(this, orderProduct))
            .toList();
        this.totalPrice = order.getTotalPrice().toString();
        this.createdAt = order.getCreatedAt();
        this.orderType = OrderEntityType.valueOf(order.getOrderType().toString());
    }

    public Order toDomain() {
        return Order.builder()
            .id(this.id)
            .userId(this.userId)
            .orderProducts(orderProducts.stream()
                .map(OrderProductEntity::toDomain)
                .toList())
            .createdAt(this.createdAt)
            .orderType(OrderType.valueOf(this.orderType.toString()))
            .build();
    }
}
