package shop.woowasap.shop.domain.out;

import java.util.Optional;
import shop.woowasap.shop.domain.out.response.ProductsPaginationResponse;
import shop.woowasap.shop.domain.product.Product;

public interface ProductRepository {

    Product persist(final Product product);

    Optional<Product> findById(final long productId);

    ProductsPaginationResponse findAllValidWithPagination(final int page, final int size);

    ProductsPaginationResponse findAllWithPagination(final int page, final int size);

    void consumeQuantityByProductId(final long productId, final long consumedQuantity);
}
