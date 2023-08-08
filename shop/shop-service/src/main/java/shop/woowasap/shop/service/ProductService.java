package shop.woowasap.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.shop.domain.product.Product;
import shop.woowasap.shop.service.repository.ProductRepository;
import shop.woowasap.shop.service.dto.RegisterProductRequest;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Long createProduct(final RegisterProductRequest createProductRequest) {
        final Product persistProduct = productRepository.save(createProductRequest.toProduct());

        return persistProduct.getId();
    }
}
