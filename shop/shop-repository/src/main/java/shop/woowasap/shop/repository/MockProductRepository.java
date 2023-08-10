package shop.woowasap.shop.repository;

import org.springframework.stereotype.Repository;
import shop.woowasap.shop.domain.product.Product;
import shop.woowasap.shop.service.repository.ProductRepository;

@Repository
public class MockProductRepository implements ProductRepository {

    @Override
    public Product save(final Product product) {
        return product;
    }
}
