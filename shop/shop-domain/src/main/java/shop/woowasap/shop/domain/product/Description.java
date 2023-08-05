package shop.woowasap.shop.domain.product;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import shop.woowasap.shop.domain.product.exception.InvalidProductDescriptionException;

@Getter
@ToString
@EqualsAndHashCode
public final class Description {

    private final String value;

    public Description(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidProductDescriptionException();
        }
    }
}