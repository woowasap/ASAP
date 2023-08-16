package shop.woowasap.shop.repository.entity.cart;

import java.io.Serializable;
import java.util.Objects;
import shop.woowasap.shop.repository.entity.product.ProductEntity;

public class CartProductId implements Serializable {

    private CartEntity cartEntity;
    private ProductEntity productEntity;

    private CartProductId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CartProductId that)) {
            return false;
        }
        return Objects.equals(cartEntity.getId(), that.cartEntity.getId()) && Objects.equals(productEntity.getId(),
                that.productEntity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartEntity.getId(), productEntity.getId());
    }
}
