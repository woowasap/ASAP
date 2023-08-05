package shop.woowasap.shop.domain.product;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import shop.woowasap.shop.domain.product.exception.InvalidProductQuantityException;

@Getter
@ToString
@EqualsAndHashCode
public final class Quantity {

    private final Long value;

    public Quantity(Long value) {
        validate(value);
        this.value = value;
    }

    private void validate(final Long value) {
        if (value == null || value < 0) {
            throw new InvalidProductQuantityException();
        }
    }
}
