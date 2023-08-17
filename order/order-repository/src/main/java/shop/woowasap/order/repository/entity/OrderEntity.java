package shop.woowasap.order.repository.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.woowasap.order.domain.Order;

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

    @OneToMany(mappedBy = "orderEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProductEntity> orderProducts = new ArrayList<>();

    @Column(name = "total_price", nullable = false)
    private String totalPrice;

    public OrderEntity(final Order order) {
        this.id = order.getId();
        this.userId = order.getUserId();
        this.orderProducts = order.getOrderProducts()
            .stream()
            .map(orderProduct -> new OrderProductEntity(this, orderProduct))
            .toList();
        this.totalPrice = order.getTotalPrice().toString();
        this.createdAt = order.getCreatedAt();
    }

    public Order toDomain() {
        return Order.builder()
            .id(this.id)
            .userId(this.userId)
            .orderProducts(orderProducts.stream()
                .map(OrderProductEntity::toDomain)
                .toList())
            .createdAt(this.createdAt)
            .build();
    }
}
