package shop.woowasap.shop.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.woowasap.shop.domain.product.Product;
import shop.woowasap.shop.domain.product.SaleTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product")
public class ProductEntity extends BaseEntity {

    @Id
    @Column(name = "product_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "description", columnDefinition = "longtext", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private String price;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "start_time", columnDefinition = "TIMESTAMP(6)", nullable = false)
    private Instant startTime;

    @Column(name = "end_time", columnDefinition = "TIMESTAMP(6)", nullable = false)
    private Instant endTime;

    public static ProductEntity from(final Product product) {
        return new ProductEntity(
            product.getId(),
            product.getName().getValue(),
            product.getDescription().getValue(),
            product.getPrice().getValue().toString(),
            product.getQuantity().getValue(),
            product.getSaleTime().getStartTime(),
            product.getSaleTime().getEndTime()
        );
    }

    public Product toDomain() {
        return Product.builder()
            .id(this.id)
            .name(this.name)
            .description(this.description)
            .price(this.price)
            .quantity(this.quantity)
            .saleTime(SaleTime.builder().startTime(startTime).endTime(endTime).build())
            .build();
    }
}
