package shop.woowasap.shop.domain.cart;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import shop.woowasap.shop.domain.exception.InvalidCartProductQuantityException;

@Getter
@ToString
@EqualsAndHashCode
public final class CartProductQuantity {

    private final Long value;

    public CartProductQuantity(final Long value) {
        validate(value);
        this.value = value;
    }

    private void validate(final Long value) {
        if (value == null || value <= 0) {
            throw new InvalidCartProductQuantityException("해당 장바구니의 상품 수량이 입력이 잘못되었습니다.");
        }
    }

    public CartProductQuantity addQuantity(final CartProductQuantity quantity) {
        return new CartProductQuantity(this.value + quantity.value);
    }
}
