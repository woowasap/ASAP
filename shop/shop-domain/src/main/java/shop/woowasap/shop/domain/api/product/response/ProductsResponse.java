package shop.woowasap.shop.domain.api.product.response;

import java.util.List;

public record ProductsResponse(List<ProductResponse> products, int page, int totalPage) {

}
