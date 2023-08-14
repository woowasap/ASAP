package shop.woowasap.shop.app.api;

import shop.woowasap.shop.app.product.Product;

public interface ProductConnector {

    Product getByProductId(final long productId);

}
