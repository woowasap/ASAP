package shop.woowasap.mock.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ProductsResponse(List<Product> products, int page, int totalPage) {

    public record Product(long productId, String name, long price, LocalDateTime startTime,
                                  LocalDateTime endTime) {

    }

}
