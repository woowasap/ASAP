package shop.woowasap.order.domain.out;

public interface Payment {

    boolean pay(long userId, final long orderId, final String totalPrice);

}
