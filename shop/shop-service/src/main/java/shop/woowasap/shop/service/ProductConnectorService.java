package shop.woowasap.shop.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.woowasap.shop.app.api.ProductConnector;
import shop.woowasap.shop.app.product.Product;
import shop.woowasap.shop.app.spi.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductConnectorService implements ProductConnector {

    private final ProductRepository productRepository;

    @Override
    public Optional<Product> findByProductId(final long productId) {
        return productRepository.findById(productId);
    }
}
