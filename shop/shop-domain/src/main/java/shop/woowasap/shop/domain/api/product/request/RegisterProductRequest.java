package shop.woowasap.shop.domain.api.product.request;

import java.time.LocalDateTime;

public record RegisterProductRequest(String name, String description, String price, long quantity,
                                     LocalDateTime startTime, LocalDateTime endTime) {

}
