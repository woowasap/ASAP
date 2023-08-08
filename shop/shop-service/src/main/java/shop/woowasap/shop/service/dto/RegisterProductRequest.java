package shop.woowasap.shop.service.dto;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import shop.woowasap.shop.domain.product.Product;

public record RegisterProductRequest(String name, String description, String price, long quantity,
                                     LocalDateTime startTime, LocalDateTime endTime) {

    public Product toProduct() {
        return Product.builder()
            .name(name)
            .description(description)
            .price(price)
            .quantity(quantity)
            .startTime(startTime.toInstant(ZoneOffset.UTC))
            .endTime(endTime.toInstant(ZoneOffset.UTC))
            .build();
    }
}
