package shop.woowasap.mock.dto;

import java.time.LocalDateTime;
import java.util.List;

public record DetailOrderResponse(long orderId, List<DetailOrderProductResponse> products,
                                  String price, long quantity, LocalDateTime createdAt) {

}
