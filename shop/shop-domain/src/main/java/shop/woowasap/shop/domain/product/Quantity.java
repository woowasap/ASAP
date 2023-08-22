package shop.woowasap.shop.domain.product;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import shop.woowasap.shop.domain.exception.InvalidProductQuantityException;

@Getter
@ToString
@EqualsAndHashCode
public final class Quantity {

    private final Long value;

    public Quantity(final Long value) {
        validate(value);
        this.value = value;
    }

    private void validate(final Long value) {
        if (value == null || value < 0) {
            throw new InvalidProductQuantityException("quantity 가 null 이거나 음수입니다.");
        }
    }
}
