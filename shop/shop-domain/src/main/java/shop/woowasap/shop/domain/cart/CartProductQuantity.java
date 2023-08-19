package shop.woowasap.shop.domain.cart;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import shop.woowasap.shop.domain.exception.InvalidCartProductQuantityException;
import shop.woowasap.shop.domain.product.Quantity;

@Getter
@ToString
@EqualsAndHashCode
public final class CartProductQuantity {

    private static final long MAX_CART_PRODUCT_QUANTITY = 10L;
    private static final long MIN_CART_PRODUCT_QUANTITY = 1L;
    private final Long value;

    public CartProductQuantity(final Long value, final Quantity quantity) {
        validate(value);
        this.value = getBoundValue(value, quantity.getValue());
    }

    public CartProductQuantity(final Long value) {
        this(value, new Quantity(MAX_CART_PRODUCT_QUANTITY));
    }

    private void validate(final Long value) {
        if (value == null) {
            throw new InvalidCartProductQuantityException("해당 장바구니의 상품 수량이 입력이 잘못되었습니다.");
        }
    }

    private long getBoundValue(final Long value, final Long remainQuantity) {
        final long maxCartProductQuantity = Math.min(MAX_CART_PRODUCT_QUANTITY, remainQuantity);
        if (value > maxCartProductQuantity) {
            return maxCartProductQuantity;
        }
        if (value < MIN_CART_PRODUCT_QUANTITY) {
            return MIN_CART_PRODUCT_QUANTITY;
        }
        return value;
    }

    public CartProductQuantity addQuantity(final CartProductQuantity quantity,
        final Quantity remainQuantity) {
        return new CartProductQuantity(this.value + quantity.value, remainQuantity);
    }
}
