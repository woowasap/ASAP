package shop.woowasap.shop.app.spi;

import java.util.Optional;
import shop.woowasap.shop.app.product.Product;
import shop.woowasap.shop.app.spi.response.ProductsPaginationResponse;

public interface ProductRepository {

    Product persist(final Product product);

    Optional<Product> findById(final long productId);

    Optional<Product> findByIdAndValidSaleTime(final long productId);

    ProductsPaginationResponse findAllValidWithPagination(final int page, final int size);

    ProductsPaginationResponse findAllWithPagination(final int page, final int size);
}
