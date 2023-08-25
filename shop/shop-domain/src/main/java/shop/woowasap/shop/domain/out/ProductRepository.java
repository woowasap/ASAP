package shop.woowasap.shop.domain.out;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import shop.woowasap.shop.domain.out.response.ProductsPaginationResponse;
import shop.woowasap.shop.domain.out.response.ProductsPaginationResponseV2;
import shop.woowasap.shop.domain.product.Product;

public interface ProductRepository {

    Product persist(final Product product);

    Optional<Product> findById(final long productId);

    ProductsPaginationResponse findAllValidWithPagination(final int page, final int size,
        final Instant nowTime);

    ProductsPaginationResponseV2 findAllValidWithPaginationV2(final Instant startTime, final Long productId, final Instant nowTime);

    ProductsPaginationResponseV2 findAllValidWithPaginationV3(final int page, final int size, final Instant nowTime);

    ProductsPaginationResponse findAllWithPagination(final int page, final int size);

    void consumeQuantityByProductId(final long productId, final long consumedQuantity);
}
