package shop.woowasap.order.domain;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import shop.woowasap.order.domain.exception.BlankProductNameException;
import shop.woowasap.order.domain.exception.InvalidPriceException;
import shop.woowasap.order.domain.exception.InvalidProductSaleTimeException;
import shop.woowasap.order.domain.exception.InvalidQuantityException;

@Getter
public class OrderProduct {

    private final long productId;
    private final String name;
    private final long quantity;
    private final BigInteger price;

    @Builder
    private OrderProduct(final long productId, final String name, final long quantity, final String price,
            final Instant startTime, final Instant endTime, final Instant nowTime) {
        validPrice(price);
        validQuantity(quantity);
        validProductSaleTime(startTime, endTime, nowTime == null ? Instant.now() : nowTime);
        validName(name);
        this.productId = productId;
        this.name = name;
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

    private void validQuantity(final long quantity) {
        if (quantity <= 0) {
            throw new InvalidQuantityException(quantity);
        }
    }

    private void validProductSaleTime(final Instant startTime, final Instant endTime, final Instant nowTime) {
        if (nowTime.isBefore(startTime) || nowTime.isAfter(endTime)) {
            throw new InvalidProductSaleTimeException(startTime, nowTime, endTime);
        }
    }

    private void validName(final String name) {
        if (name == null || name.isBlank()) {
            throw new BlankProductNameException();
        }
    }
}
