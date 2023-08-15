package shop.woowasap.mock.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(long orderId, List<OrderProductResponse> products, String price,
                            long quantity, LocalDateTime createdAt) {

}
