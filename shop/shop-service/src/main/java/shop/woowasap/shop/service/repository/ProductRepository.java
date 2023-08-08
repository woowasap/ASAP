package shop.woowasap.shop.service.repository;

import java.util.Optional;
import shop.woowasap.shop.domain.Product;

public interface ProductRepository {

    void save(Product product);

    Optional<Product> findById(long productId);
}
