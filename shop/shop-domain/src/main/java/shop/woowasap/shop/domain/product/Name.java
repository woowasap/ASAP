package shop.woowasap.shop.domain.product;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import shop.woowasap.shop.domain.exception.InvalidProductNameException;

@Getter
@ToString
@EqualsAndHashCode
public final class Name {

    private final String value;

    public Name(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidProductNameException("name 이 비어있거나 Null 입니다.");
        }
    }
}
