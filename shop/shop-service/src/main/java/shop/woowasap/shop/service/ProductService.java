package shop.woowasap.shop.service;

import static shop.woowasap.shop.service.mapper.ProductMapper.toDomain;
import static shop.woowasap.shop.service.mapper.ProductMapper.toProductsAdminResponse;

import java.text.MessageFormat;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.core.id.api.IdGenerator;
import shop.woowasap.core.util.time.TimeUtil;
import shop.woowasap.shop.domain.exception.NotExistsProductException;
import shop.woowasap.shop.domain.exception.SaleEndedProductException;
import shop.woowasap.shop.domain.in.product.ProductUseCase;
import shop.woowasap.shop.domain.in.product.request.RegisterProductRequest;
import shop.woowasap.shop.domain.in.product.request.UpdateProductRequest;
import shop.woowasap.shop.domain.in.product.response.ProductDetailsResponse;
import shop.woowasap.shop.domain.in.product.response.ProductsAdminResponse;
import shop.woowasap.shop.domain.in.product.response.ProductsResponse;
import shop.woowasap.shop.domain.out.ProductRepository;
import shop.woowasap.shop.domain.out.response.ProductsPaginationAdminResponse;
import shop.woowasap.shop.domain.out.response.ProductsPaginationResponse;
import shop.woowasap.shop.domain.product.Product;
import shop.woowasap.shop.service.mapper.ProductMapper;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService implements ProductUseCase {

    private final ProductRepository productRepository;
    private final IdGenerator idGenerator;
    private final TimeUtil timeUtil;

    @Override
    @Transactional
    @CacheEvict(value = "products", allEntries = true, cacheManager = "cacheManager")
    public void update(final long productId, final UpdateProductRequest updateProductRequest) {
        final Product product = getProduct(productId);

        final Product updateProduct = product.update(
            updateProductRequest.name(),
            updateProductRequest.description(),
            updateProductRequest.price(),
            updateProductRequest.quantity(),
            updateProductRequest.startTime(),
            updateProductRequest.endTime(),
            timeUtil.now()
        );

        productRepository.persist(updateProduct);
    }

    @CacheEvict(value = "products", allEntries = true, cacheManager = "cacheManager")
    @Override
    @Transactional
    public Long registerProduct(final RegisterProductRequest registerProductRequest) {
        final Product persistProduct = productRepository.persist(
            toDomain(idGenerator, registerProductRequest, timeUtil.now()));

        return persistProduct.getId();
    }

    @Override
    @Cacheable(value = "products", key = "#startTime+':'+#productId", cacheManager = "cacheManager")
    public ProductsResponse getValidProducts(final String startTime, final Long productId) {
        final ProductsPaginationResponse response = productRepository.findAllValidWithPagination(
            Instant.parse(startTime), productId, timeUtil.now());

        return ProductMapper.toProductsResponse(response);
    }

    @Override
    public ProductsAdminResponse getProductsInAdmin(final int page, final int size) {
        final ProductsPaginationAdminResponse paginationResponse = productRepository
            .findAllWithPagination(page, size);

        return toProductsAdminResponse(paginationResponse);
    }

    @Override
    public ProductDetailsResponse getByProductId(final long productId) {
        final Product product = getProduct(productId);

        if (product.isEndTimeBefore(timeUtil.now())) {
            throw new SaleEndedProductException(
                MessageFormat.format(
                    "판매가 종료된 product 입니다. productId : \"{0}\"",
                    productId)
            );
        }

        return ProductMapper.toProductResponse(product);
    }

    @Override
    public ProductDetailsResponse getByProductIdWithAdmin(final long productId) {
        final Product persistProduct = getProduct(productId);

        return ProductMapper.toProductResponse(persistProduct);
    }

    private Product getProduct(final long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new NotExistsProductException(
                MessageFormat.format("productId 에 해당하는 Product 가 존재하지 않습니다. productId : \"{0}\"",
                    productId)
            ));
    }
}
