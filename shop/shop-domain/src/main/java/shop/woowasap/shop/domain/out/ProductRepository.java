package shop.woowasap.shop.domain.out;

import java.time.Instant;
import java.util.Optional;
import shop.woowasap.shop.domain.out.response.ProductsPaginationAdminResponse;
import shop.woowasap.shop.domain.out.response.ProductsPaginationResponse;
import shop.woowasap.shop.domain.product.Product;

public interface ProductRepository {

    Product persist(final Product product);

    Optional<Product> findById(final long productId);

    ProductsPaginationResponse findAllValidWithPagination(final Instant startTime, final Long productId, final Instant nowTime);

    ProductsPaginationAdminResponse findAllWithPagination(final int page, final int size);

    void consumeQuantityByProductId(final long productId, final long consumedQuantity);
}
