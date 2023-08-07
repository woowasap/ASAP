package shop.woowasap.shop.service.repository;

import java.util.Optional;
import shop.woowasap.shop.domain.Product;

public interface ProductRepository {

    Optional<Product> findById(long productId);

}
