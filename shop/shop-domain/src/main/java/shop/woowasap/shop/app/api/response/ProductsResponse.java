package shop.woowasap.shop.app.api.response;

import java.time.LocalDateTime;
import java.util.List;

public record ProductsResponse(List<ProductResponse> products, int page, int totalPage) {

    public record ProductResponse(long productId, String name, String price, LocalDateTime startTime,
                          LocalDateTime endTime) {

    }

}
