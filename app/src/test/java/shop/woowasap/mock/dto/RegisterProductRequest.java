package shop.woowasap.mock.dto;

import java.time.LocalDateTime;

public record RegisterProductRequest(String name, String description, String price, int quantity,
                                     LocalDateTime startTime, LocalDateTime endTime) {

}