package shop.woowasap.shop.domain.in.product.response;

import java.util.List;

public record ProductsResponse(List<ProductResponse> products, boolean hasNext) {

}
