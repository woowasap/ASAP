package shop.woowasap.order.domain;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import shop.woowasap.order.domain.exception.InvalidOrderProductException;

@Getter
public class Order {

    private final long id;
    private final List<OrderProduct> orderProducts;
    private final BigInteger totalPrice;

    @Builder
    private Order(long id, List<OrderProduct> orderProducts) {
        validOrderProducts(orderProducts);
        this.id = id;
        this.orderProducts = orderProducts;
        this.totalPrice = calculatePrice();
    }

    private void validOrderProducts(List<OrderProduct> orderProducts) {
        if (Objects.isNull(orderProducts) || orderProducts.isEmpty()) {
            throw new InvalidOrderProductException(orderProducts);
        }
    }

    private BigInteger calculatePrice() {
        BigInteger ans = BigInteger.ZERO;
        for (OrderProduct orderProduct : orderProducts) {
            ans = ans.add(orderProduct.getPrice());
        }
        return ans;
    }
}
