package shop.woowasap.order.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.woowasap.order.domain.OrderProduct;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "order_product")
@IdClass(OrderProductId.class)
public class OrderProductEntity extends BaseEntity {

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
}
