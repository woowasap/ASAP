package shop.woowasap.order.domain.in;

import shop.woowasap.order.domain.in.request.OrderProductRequest;
import shop.woowasap.order.domain.in.response.DetailOrderResponse;
import shop.woowasap.order.domain.in.response.OrderIdResponse;
import shop.woowasap.order.domain.in.response.OrdersResponse;

public interface OrderUseCase {

    OrderIdResponse orderProduct(final OrderProductRequest orderProductRequest);

    OrderIdResponse orderCartByCartIdAndUserId(final long cartId, final long userId);

    OrdersResponse getOrderByUserId(final long userId, final int page, final int size);

    DetailOrderResponse getOrderByOrderIdAndUserId(final long orderId, final long userId);

    void cancelOrder(final long orderId, final long userId);
}
