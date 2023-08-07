package shop.woowasap.mock.dto;

import java.time.LocalDateTime;

public record ProductResponse(long productId, String name, String description, String price,
                              int quantity, LocalDateTime startTime, LocalDateTime endTime) {

}
