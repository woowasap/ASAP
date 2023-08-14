package shop.woowasap.order.domain;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import shop.woowasap.order.domain.exception.InvalidPriceException;
import shop.woowasap.order.domain.exception.InvalidProductSaleTimeException;
import shop.woowasap.order.domain.exception.InvalidQuantityException;

@Getter
public class OrderProduct {

    private final long productId;
    private final long quantity;
    private final BigInteger price;

    @Builder
    private OrderProduct(final long productId, final long quantity, final String price,
            final Instant startTime, final Instant endTime) {
        validPrice(price);
        validQuantity(quantity);
        validateProductSaleTime(startTime, endTime);
        this.productId = productId;
        this.quantity = quantity;
        this.price = new BigInteger(price).multiply(BigInteger.valueOf(quantity));
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

    private void validQuantity(final long quantity) {
        if (quantity <= 0) {
            throw new InvalidQuantityException(quantity);
        }
    }

    private void validateProductSaleTime(final Instant startTime, final Instant endTime) {
        final Instant currentTime = Instant.now();
        if (currentTime.isBefore(startTime) || currentTime.isAfter(endTime)) {
            throw new InvalidProductSaleTimeException(startTime, currentTime, endTime);
        }
    }
}
