package shop.woowasap.shop.domain.spi;

import java.util.Optional;
import shop.woowasap.shop.domain.product.Product;
import shop.woowasap.shop.domain.spi.response.ProductsPaginationResponse;

public interface ProductRepository {

    Product persist(final Product product);

    Optional<Product> findById(final long productId);

    Optional<Product> findByIdAndValidSaleTime(final long productId);

    ProductsPaginationResponse findAllValidWithPagination(final int page, final int size);

    ProductsPaginationResponse findAllWithPagination(final int page, final int size);
}