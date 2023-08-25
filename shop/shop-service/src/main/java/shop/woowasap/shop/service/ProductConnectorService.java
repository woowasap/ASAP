package shop.woowasap.shop.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.shop.domain.in.product.ProductConnector;
import shop.woowasap.shop.domain.product.Product;
import shop.woowasap.shop.domain.out.ProductRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductConnectorService implements ProductConnector {

    private final ProductRepository productRepository;

    @Override
    public Optional<Product> findByProductId(final long productId) {
        return productRepository.findById(productId);
    }

    @Override
    @Transactional
    public void consumeProductByProductId(final long productId, final long consumedQuantity) {
        productRepository.consumeQuantityByProductId(productId, consumedQuantity);
    }
}
