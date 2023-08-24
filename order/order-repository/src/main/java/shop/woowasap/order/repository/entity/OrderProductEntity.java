package shop.woowasap.order.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.Instant;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;
import shop.woowasap.order.domain.OrderProduct;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "order_product")
@IdClass(OrderProductId.class)
public class OrderProductEntity extends BaseEntity implements Persistable<OrderProductId> {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity orderEntity;

    @Id
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private String price;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Transient
    private boolean isNew = false;

    protected OrderProductEntity(final OrderEntity orderEntity, final OrderProduct orderProduct) {
        this.orderEntity = orderEntity;
        this.productId = orderProduct.getProductId();
        this.name = orderProduct.getName();
        this.price = orderProduct.getPrice().toString();
        this.quantity = orderProduct.getQuantity();
    }

    protected OrderProduct toDomain() {
        return OrderProduct.builder()
            .productId(this.productId)
            .name(this.name)
            .price(this.price)
            .quantity(this.quantity)
            .startTime(Instant.MIN)
            .endTime(Instant.MAX)
            .build();
    }

    @Override
    public OrderProductId getId() {
        return new OrderProductId(orderEntity, productId);
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
        if (!(o instanceof final OrderProductEntity that)) {
            return false;
        }
        return Objects.equals(orderEntity.getId(), that.orderEntity.getId()) && Objects.equals(
            productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderEntity.getId(), productId);
    }
}
