package shop.woowasap.shop.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import shop.woowasap.shop.domain.out.ProductRepository;
import shop.woowasap.shop.domain.out.response.ProductsPaginationAdminResponse;
import shop.woowasap.shop.domain.out.response.ProductsPaginationResponse;
import shop.woowasap.shop.domain.product.Product;
import shop.woowasap.shop.repository.entity.ProductEntity;
import shop.woowasap.shop.repository.jpa.ProductJpaRepository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private static final String COLUMN_START_TIME = "startTime";
    private static final String COLUMN_PRODUCT_ID = "id";

    private final ProductJpaRepository productJpaRepository;

    @Override
    public Product persist(final Product product) {
        final ProductEntity productEntity = productJpaRepository.save(ProductEntity.from(product));
        return productEntity.toDomain();
    }

    @Override
    public Optional<Product> findById(final long productId) {
        return productJpaRepository.findById(productId)
            .map(ProductEntity::toDomain);
    }

    @Override
    public ProductsPaginationResponse findAllValidWithPagination(
        final Instant startTime,
        final Long productId,
        final Instant nowTime
    ) {
        final PageRequest pageRequest = PageRequest.of(0, 20,
            Sort.by(COLUMN_START_TIME, COLUMN_PRODUCT_ID).ascending());

        final Slice<ProductEntity> pagination = productJpaRepository.
            findAllByEndTimeAfter(nowTime, startTime, productId,
                pageRequest);

        final List<Product> products = pagination.get()
            .map(ProductEntity::toDomain)
            .toList();

        return new ProductsPaginationResponse(products, pagination.hasNext());
    }

    @Override
    public ProductsPaginationAdminResponse findAllWithPagination(final int page, final int size) {
        final PageRequest pageRequest = PageRequest.of(page - 1, size,
            Sort.by(COLUMN_START_TIME).ascending());
        final Page<ProductEntity> pagination = productJpaRepository.findAll(pageRequest);

        final List<Product> products = pagination.get()
            .map(ProductEntity::toDomain)
            .toList();

        return new ProductsPaginationAdminResponse(products, page, pagination.getTotalPages());
    }

    @Override
    public void consumeQuantityByProductId(long productId, long consumedQuantity) {
        productJpaRepository.consumeQuantity(productId, consumedQuantity);
    }
}
