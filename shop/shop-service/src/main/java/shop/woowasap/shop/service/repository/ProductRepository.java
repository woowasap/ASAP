package shop.woowasap.shop.service.repository;

import shop.woowasap.shop.domain.product.Product;

public interface ProductRepository {

    Product save(final Product product);
}
