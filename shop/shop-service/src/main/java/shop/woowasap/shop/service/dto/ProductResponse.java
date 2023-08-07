package shop.woowasap.shop.service.dto;

import java.time.LocalDateTime;

public record ProductResponse(long productId, String name, String description, String price,
                              long quantity, LocalDateTime startTime, LocalDateTime endTime) {

}
