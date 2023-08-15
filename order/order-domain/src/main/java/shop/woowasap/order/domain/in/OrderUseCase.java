package shop.woowasap.order.domain.in;

import shop.woowasap.order.domain.in.request.OrderProductRequest;
import shop.woowasap.order.domain.in.response.OrdersResponse;

public interface OrderUseCase {

    long orderProduct(final OrderProductRequest orderProductRequest);

    OrdersResponse getOrderByUserId(final long userId, final int page, final int size);
}
