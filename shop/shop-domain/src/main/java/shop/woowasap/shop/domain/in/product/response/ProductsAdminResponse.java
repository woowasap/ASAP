package shop.woowasap.shop.domain.in.product.response;

import java.util.List;

public record ProductsAdminResponse(List<ProductResponse> products, int page, int totalPage) {

}
