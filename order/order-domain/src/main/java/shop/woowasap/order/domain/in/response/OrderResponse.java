package shop.woowasap.order.domain.in.response;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(long orderId, List<OrderProductResponse> products, String totalPrice,
                            LocalDateTime createdAt) {

}
