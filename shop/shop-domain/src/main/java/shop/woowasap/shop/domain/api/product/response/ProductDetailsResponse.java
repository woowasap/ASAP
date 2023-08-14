package shop.woowasap.shop.domain.api.product.response;

import java.time.LocalDateTime;

public record ProductDetailsResponse(long productId, String name, String description, String price,
                                     long quantity, LocalDateTime startTime, LocalDateTime endTime) {

}
