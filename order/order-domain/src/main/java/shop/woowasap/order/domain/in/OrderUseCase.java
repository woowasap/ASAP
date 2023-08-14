package shop.woowasap.order.domain.in;

import shop.woowasap.order.domain.in.request.OrderProductRequest;

public interface OrderUseCase {

    long orderProduct(final OrderProductRequest orderProductRequest);

}
