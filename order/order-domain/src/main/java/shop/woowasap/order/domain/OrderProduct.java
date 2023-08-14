package shop.woowasap.order.domain;

import java.math.BigInteger;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import shop.woowasap.order.domain.exception.InvalidPriceException;
import shop.woowasap.order.domain.exception.InvalidQuantityException;

@Getter
public class OrderProduct {

    private final long productId;
    private final int quantity;
    private final BigInteger price;

    @Builder
    private OrderProduct(final long productId, final int quantity, final String price) {
        validPrice(price);
        validQuantity(quantity);
        this.productId = productId;
        this.quantity = quantity;
        this.price = new BigInteger(price);
    }

    private void validPrice(final String price) {
        validPriceDoesNotNull(price);
        validPriceNotNumber(price);
    }

    private void validPriceDoesNotNull(final String price) {
        if (Objects.isNull(price)) {
            throw new InvalidPriceException(price);
        }
    }

    private void validPriceNotNumber(final String price) {
        try {
            new BigInteger(price);
        } catch (NumberFormatException numberFormatException) {
            throw new InvalidPriceException(price);
        }
    }

    private void validQuantity(final int quantity) {
        if (quantity <= 0) {
            throw new InvalidQuantityException(quantity);
        }
    }

    public String totalPrice() {
        return price.multiply(BigInteger.valueOf(quantity)).toString();
    }
}
