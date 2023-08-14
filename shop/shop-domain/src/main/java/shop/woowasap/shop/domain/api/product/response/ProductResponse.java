package shop.woowasap.shop.domain.api.product.response;

import java.time.LocalDateTime;

public record ProductResponse(long productId, String name, String price, LocalDateTime startTime,
                              LocalDateTime endTime) {

}