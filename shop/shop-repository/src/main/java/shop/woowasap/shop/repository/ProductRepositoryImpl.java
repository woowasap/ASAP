package shop.woowasap.shop.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import shop.woowasap.shop.domain.out.ProductRepository;
import shop.woowasap.shop.domain.out.response.ProductsPaginationResponse;
import shop.woowasap.shop.domain.product.Product;
import shop.woowasap.shop.repository.entity.ProductEntity;
import shop.woowasap.shop.repository.jpa.ProductJpaRepository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

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
    public ProductsPaginationResponse findAllValidWithPagination(final int page, final int size,
        final Instant nowTime) {
        final PageRequest pageRequest = PageRequest.of(page - 1, size,
            Sort.by("startTime").ascending());
        final Page<ProductEntity> pagination = productJpaRepository.findAllByEndTimeAfter(
            nowTime, pageRequest);

        final List<Product> products = pagination.get()
            .map(ProductEntity::toDomain)
            .toList();

        return new ProductsPaginationResponse(products, page, pagination.getTotalPages());
    }

    @Override
    public ProductsPaginationResponse findAllWithPagination(final int page, final int size) {
        final PageRequest pageRequest = PageRequest.of(page - 1, size,
            Sort.by("startTime").ascending());
        final Page<ProductEntity> pagination = productJpaRepository.findAll(pageRequest);

        final List<Product> products = pagination.get()
            .map(ProductEntity::toDomain)
            .toList();

        return new ProductsPaginationResponse(products, page, pagination.getTotalPages());
    }

    @Override
    public void consumeQuantityByProductId(long productId, long consumedQuantity) {
        productJpaRepository.consumeQuantity(productId, consumedQuantity);
    }
}
