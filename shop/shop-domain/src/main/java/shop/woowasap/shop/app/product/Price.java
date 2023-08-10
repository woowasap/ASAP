package shop.woowasap.shop.app.product;

import java.math.BigInteger;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import shop.woowasap.shop.app.exception.InvalidProductPriceException;

@Getter
@ToString
@EqualsAndHashCode
public final class Price {

    private static final String NUMBER_PATTERN_REGEX = "\\d+";
    private final BigInteger value;

    public Price(final String value) {
        validate(value);
        this.value = new BigInteger(value);
    }

    private void validate(final String value) {
        if (value == null || value.isBlank() || !value.matches(NUMBER_PATTERN_REGEX)
            || new BigInteger(value).compareTo(BigInteger.ZERO) <= 0) {
            throw new InvalidProductPriceException();
        }
    }
}
