package shop.woowasap.shop.domain.out.response;

import java.util.List;
import shop.woowasap.shop.domain.product.Product;

public record ProductsPaginationAdminResponse(List<Product> products, int page, int totalPage) {

}
