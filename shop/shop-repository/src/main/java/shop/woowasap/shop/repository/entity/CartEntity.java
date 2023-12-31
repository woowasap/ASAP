package shop.woowasap.shop.repository.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import shop.woowasap.shop.domain.cart.Cart;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cart")
public class CartEntity extends BaseEntity {

    @Id
    @Column(name = "cart_id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "cartEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartProductEntity> cartProducts = new ArrayList<>();

    public CartEntity(final Long cartId, final Long userId) {
        this.id = cartId;
        this.userId = userId;
    }

    public static CartEntity from(final Cart cart) {
        final List<CartProductEntity> cartProductEntities = cart.getCartProducts().stream()
            .map(CartProductEntity::from).toList();
        final CartEntity cartEntity = new CartEntity(cart.getId(), cart.getUserId(),
            cartProductEntities);
        cartProductEntities.forEach(
            cartProductEntity -> cartProductEntity.setCartEntity(cartEntity));
        return cartEntity;
    }

    public Cart toDomain() {
        return Cart.builder()
            .id(this.id)
            .userId(this.userId)
            .cartProducts(this.cartProducts.stream().map(CartProductEntity::toDomain)
                .collect(Collectors.toList()))
            .build();
    }

    public List<CartProductEntity> getCartProducts() {
        return cartProducts;
    }
}
