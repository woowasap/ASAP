package shop.woowasap.shop.app.spi;

import java.util.Optional;
import shop.woowasap.shop.app.product.Product;

public interface ProductRepository {

    Product persist(Product product);

    Optional<Product> findById(long productId);
}
