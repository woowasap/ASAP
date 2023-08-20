package shop.woowasap.shop.domain.product;

import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import shop.woowasap.shop.domain.exception.InvalidProductSaleTimeException;
import shop.woowasap.shop.domain.exception.ProductModificationPermissionException;

@Getter
@ToString
@EqualsAndHashCode
public final class Product {

    private final Long id;
    private final Name name;
    private final Description description;
    private final Price price;
    private final Quantity quantity;
    private final SaleTime saleTime;

    @Builder
    private Product(
        final Long id,
        final String name,
        final String description,
        final String price,
        final Long quantity,
        final SaleTime saleTime
    ) {
        validateSaleTime(saleTime);
        this.id = id;
        this.name = new Name(name);
        this.description = new Description(description);
        this.price = new Price(price);
        this.quantity = new Quantity(quantity);
        this.saleTime = saleTime;
    }

    private void validateSaleTime(final SaleTime saleTime) {
        if (Objects.isNull(saleTime)) {
            throw new InvalidProductSaleTimeException("saleTime 이 존재하지 않습니다.");
        }
    }

    public Product update(
        final String name,
        final String description,
        final String price,
        final long quantity,
        final LocalDateTime startTime,
        final LocalDateTime endTime,
        final Instant nowTime
    ) {
        validateUpdateTime(nowTime);

        return Product.builder()
            .id(id)
            .name(name)
            .description(description)
            .price(price)
            .quantity(quantity)
            .saleTime(SaleTime.builder()
                .startTime(startTime.atZone(ZoneOffset.UTC).toInstant())
                .endTime(endTime.atZone(ZoneOffset.UTC).toInstant())
                .nowTime(nowTime)
                .build())
            .build();
    }

    private void validateUpdateTime(final Instant nowTime) {
        if (isOnSale(nowTime)) {
            throw new ProductModificationPermissionException(
                MessageFormat.format("현재 판매 중인 Product 는 수정할 수 없습니다. productId : \"{0}\"", id)
            );
        }
    }

    private boolean isOnSale(final Instant nowTime) {
        return saleTime.isOnSale(nowTime);
    }

    public boolean isEndTimeBefore(final Instant time) {
        return saleTime.isEndTimeBefore(time);
    }
}
