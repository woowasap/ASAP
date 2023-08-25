package shop.woowasap.order.domain.in.response;

import java.time.LocalDateTime;
import java.util.List;

public record DetailOrderResponse(long orderId, List<DetailOrderProductResponse> products,
                                  String totalPrice, String type, LocalDateTime createdAt) {

}
