package shop.woowasap.shop.domain.api.product;

import java.util.Optional;
import shop.woowasap.shop.domain.product.Product;

public interface ProductConnector {

    Optional<Product> findByProductId(final long productId);

}
