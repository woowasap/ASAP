package shop.woowasap.shop.service;

import static shop.woowasap.shop.service.mapper.ProductMapper.toDomain;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.core.id.api.IdGenerator;
import shop.woowasap.shop.app.api.ProductUseCase;
import shop.woowasap.shop.app.api.request.RegisterProductRequest;
import shop.woowasap.shop.app.api.request.UpdateProductRequest;
import shop.woowasap.shop.app.product.Product;
import shop.woowasap.shop.app.exception.CannotFindProductException;
import shop.woowasap.shop.app.spi.ProductRepository;
import shop.woowasap.shop.app.api.response.ProductsResponse;
import shop.woowasap.shop.app.spi.response.ProductsPaginationResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService implements ProductUseCase {

    private final ProductRepository productRepository;
    private final IdGenerator idGenerator;

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

    private Product getProduct(final long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new CannotFindProductException(
                MessageFormat.format("productId 에 해당하는 Product 가 존재하지 않습니다. productId : \"{0}\"",
                    productId)
            ));
    }

    @Override
    @Transactional
    public Long registerProduct(final RegisterProductRequest registerProductRequest) {
        final Product persistProduct = productRepository.persist(
            toDomain(idGenerator, registerProductRequest));

        return persistProduct.getId();
    }

    public ProductsResponse getValidProducts(final int page, final int size) {
        ProductsPaginationResponse pagination = productRepository.findAllValidWithPagination(
            page, size);
        return new ProductsResponse(toProductsOrProductsResponse(pagination.products()), page,
            pagination.totalPage());
    }

    private List<ProductsResponse.Product> toProductsOrProductsResponse (List<Product> products) {

        return products.stream()
            .map(product -> new ProductsResponse.Product(
                product.getId(),
                product.getName().getValue(),
                product.getPrice().getValue().longValue(),
                LocalDateTime.ofInstant(product.getStartTime(), ZoneId.of("Asia/Seoul")),
                LocalDateTime.ofInstant(product.getEndTime(), ZoneId.of("Asia/Seoul"))
            ))
            .toList();
    }

}
