package shop.woowasap.shop.app.api;

import java.util.Optional;
import shop.woowasap.shop.app.product.Product;

public interface ProductConnector {

    Optional<Product> findByProductId(final long productId);

}
