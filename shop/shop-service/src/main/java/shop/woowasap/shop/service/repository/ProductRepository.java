package shop.woowasap.shop.service.repository;

import java.util.Optional;
import shop.woowasap.shop.domain.product.Product;

public interface ProductRepository {

    Product persist(Product product);

    Optional<Product> findById(long productId);
}
