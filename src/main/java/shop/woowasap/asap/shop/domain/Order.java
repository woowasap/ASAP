package shop.woowasap.asap.shop.domain;

import java.math.BigInteger;
import java.time.Instant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

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
