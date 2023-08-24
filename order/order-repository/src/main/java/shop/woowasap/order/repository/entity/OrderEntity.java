package shop.woowasap.order.repository.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.OrderType;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class OrderEntity extends BaseEntity implements Persistable<Long> {

    @Id
    @Column(name = "order_id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "orderEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProductEntity> orderProducts = new ArrayList<>();

    @Column(name = "total_price", nullable = false)
    private String totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", length = 10, nullable = false)
    private OrderEntityType orderType;

    @Transient
    private boolean isNew = true;

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

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostLoad
    @PostPersist
    protected void loadOrPersist() {
        this.isNew = false;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final OrderEntity that)) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
