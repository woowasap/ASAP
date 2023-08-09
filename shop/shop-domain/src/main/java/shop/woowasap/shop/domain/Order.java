package shop.woowasap.shop.domain;

import java.math.BigInteger;
import java.time.Instant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import shop.woowasap.shop.domain.product.Product;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Order {

    private final Long id;
    private final BigInteger totalPrice;
    private final Product product;
    private final Long productCount;
    private final Instant createdAt;

}
