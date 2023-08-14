package shop.woowasap.shop.domain.spi.response;

import java.util.List;
import shop.woowasap.shop.domain.product.Product;

public record ProductsPaginationResponse(List<Product> products, int page, int totalPage) {

}
