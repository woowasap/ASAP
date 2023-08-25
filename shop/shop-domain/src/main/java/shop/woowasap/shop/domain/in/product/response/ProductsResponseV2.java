package shop.woowasap.shop.domain.in.product.response;

import java.util.List;

public record ProductsResponseV2(List<ProductResponse> products, boolean hasNext) {

}
