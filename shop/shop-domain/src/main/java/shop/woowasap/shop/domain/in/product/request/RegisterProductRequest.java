package shop.woowasap.shop.domain.in.product.request;

import java.time.LocalDateTime;

public record RegisterProductRequest(String name, String description, String price, long quantity,
                                     LocalDateTime startTime, LocalDateTime endTime) {

}
