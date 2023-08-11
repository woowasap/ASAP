package shop.woowasap.shop.app.api.response;

import java.time.LocalDateTime;

public record ProductResponse(long productId, String name, String description, String price,
                              long quantity, LocalDateTime startTime, LocalDateTime endTime) {

}
