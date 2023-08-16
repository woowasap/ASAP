package shop.woowasap.shop.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
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

}
