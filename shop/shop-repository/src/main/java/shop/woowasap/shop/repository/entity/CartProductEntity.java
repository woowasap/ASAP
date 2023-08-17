package shop.woowasap.shop.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.woowasap.shop.domain.cart.CartProduct;
import shop.woowasap.shop.domain.cart.CartProductQuantity;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "cart_product")
@IdClass(CartProductId.class)
public class CartProductEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private CartEntity cartEntity;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;

    @Column(name = "quantity")
    private Long quantity;

    protected CartProductEntity(final ProductEntity productEntity, final Long quantity) {
        this.productEntity = productEntity;
        this.quantity = quantity;
    }

    public static CartProductEntity from(final CartProduct cartProduct) {
        return new CartProductEntity(
            ProductEntity.from(cartProduct.getProduct()),
            cartProduct.getQuantity().getValue());
    }

    public CartProduct toDomain() {
        return CartProduct.builder()
            .product(productEntity.toDomain())
            .quantity(new CartProductQuantity(quantity))
            .build();
    }

    void setCartEntity(final CartEntity cartEntity) {
        this.cartEntity = cartEntity;
    }
}
