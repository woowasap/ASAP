package shop.woowasap.shop.service;

import static shop.woowasap.shop.service.mapper.ProductMapper.toDomain;
import static shop.woowasap.shop.service.mapper.ProductMapper.toProductsResponse;

import java.text.MessageFormat;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.core.id.api.IdGenerator;
import shop.woowasap.shop.app.api.ProductUseCase;
import shop.woowasap.shop.app.api.request.RegisterProductRequest;
import shop.woowasap.shop.app.api.request.UpdateProductRequest;
import shop.woowasap.shop.app.api.response.ProductResponse;
import shop.woowasap.shop.app.api.response.ProductsResponse;
import shop.woowasap.shop.app.exception.CannotFindProductException;
import shop.woowasap.shop.app.product.Product;
import shop.woowasap.shop.app.spi.ProductRepository;
import shop.woowasap.shop.app.spi.response.ProductsPaginationResponse;
import shop.woowasap.shop.service.mapper.ProductMapper;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService implements ProductUseCase {

    private final ProductRepository productRepository;
    private final IdGenerator idGenerator;

    @Value("${shop.woowasap.locale:Asia/Seoul}")
    private String locale;
    @Value("${shop.woowasap.offsetid:+09:00}")
    private String offsetId;

    @Override
    @Transactional
    public void update(final long productId, final UpdateProductRequest updateProductRequest) {
        final Product product = getProduct(productId);
        final Product updateProduct = product.update(
            updateProductRequest.name(),
            updateProductRequest.description(),
            updateProductRequest.price(),
            updateProductRequest.quantity(),
            updateProductRequest.startTime(),
            updateProductRequest.endTime()
        );

        productRepository.persist(updateProduct);
    }

    @Override
    @Transactional
    public Long registerProduct(final RegisterProductRequest registerProductRequest) {
        final Product persistProduct = productRepository.persist(
            toDomain(idGenerator, registerProductRequest, offsetId));

        return persistProduct.getId();
    }

    @Override
    public ProductsResponse getValidProducts(final int page, final int size) {
        final ProductsPaginationResponse pagination = productRepository.findAllValidWithPagination(page,
            size);

        return ProductMapper.toProductsResponse(pagination, ZoneId.of(locale));
    }

    @Override
    public ProductsResponse getProductsInAdmin(final int page, final int size) {
        final ProductsPaginationResponse paginationResponse = productRepository
            .findAllWithPagination(page, size);

        return toProductsResponse(paginationResponse, ZoneId.of(locale));
    }

    @Override
    public ProductResponse getById(final long id) {
        final Product persistProduct = productRepository.findByIdAndValidSaleTime(id)
            .orElseThrow(() -> new CannotFindProductException(
                MessageFormat.format("productId 에 해당하는 Product 가 존재하지 않습니다. productId : \"{0}\"",
                    id)));

        return ProductMapper.toProductResponse(persistProduct, ZoneId.of(locale));
    }

    @Override
    public ProductResponse getByIdWithAdmin(final long productId) {
        final Product persistProduct = getProduct(productId);

        return ProductMapper.toProductResponse(persistProduct, ZoneId.of(locale));
    }

    private Product getProduct(final long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new CannotFindProductException(
                MessageFormat.format("productId 에 해당하는 Product 가 존재하지 않습니다. productId : \"{0}\"",
                    productId)
            ));
    }
}
