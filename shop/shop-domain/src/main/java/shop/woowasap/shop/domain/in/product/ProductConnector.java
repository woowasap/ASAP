package shop.woowasap.shop.domain.in.product;

import java.util.Optional;
import shop.woowasap.shop.domain.product.Product;

public interface ProductConnector {

    Optional<Product> findByProductId(final long productId);

    void consumeProductByProductId(final long productId, final long quantity);
}
