package shop.woowasap.shop.dto;

import java.time.LocalDateTime;

public record UpdateProductRequest(String name, String description, String price, int quantity,
                                  LocalDateTime startTime, LocalDateTime endTime) {

}
