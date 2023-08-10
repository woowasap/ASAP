package shop.woowasap.shop.app.spi.response;

import java.util.List;
import shop.woowasap.shop.app.product.Product;

public record ProductsPaginationResponse(List<Product> products, int page, int totalPage) {

}