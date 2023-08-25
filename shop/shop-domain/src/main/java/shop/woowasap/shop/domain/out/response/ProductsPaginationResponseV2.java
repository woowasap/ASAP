package shop.woowasap.shop.domain.out.response;

import java.util.List;
import shop.woowasap.shop.domain.product.Product;

public record ProductsPaginationResponseV2(List<Product> products, boolean hasNext) {

}
