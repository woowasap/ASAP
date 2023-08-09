package shop.woowasap.shop.service;

import static shop.woowasap.shop.service.mapper.ProductMapper.toDomain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.core.id.api.IdGenerator;
import shop.woowasap.shop.domain.product.Product;
import shop.woowasap.shop.service.dto.RegisterProductRequest;
import shop.woowasap.shop.service.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final IdGenerator idGenerator;

    @Transactional
    public Long registerProduct(final RegisterProductRequest registerProductRequest) {
        final Product persistProduct = productRepository.save(
            toDomain(idGenerator, registerProductRequest));

        return persistProduct.getId();
    }
}
