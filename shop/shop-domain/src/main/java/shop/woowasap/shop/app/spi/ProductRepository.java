package shop.woowasap.shop.app.spi;

import java.util.Optional;
import shop.woowasap.shop.app.product.Product;
import shop.woowasap.shop.app.spi.response.ProductsPaginationResponse;

public interface ProductRepository {

    Product persist(Product product);

    Optional<Product> findById(long productId);

    ProductsPaginationResponse findAllValidWithPagination(int page, int size);
}
