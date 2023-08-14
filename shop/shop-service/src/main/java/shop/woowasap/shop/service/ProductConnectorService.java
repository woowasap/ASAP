package shop.woowasap.shop.service;

import java.text.MessageFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.woowasap.shop.app.api.ProductConnector;
import shop.woowasap.shop.app.exception.CannotFindProductException;
import shop.woowasap.shop.app.product.Product;
import shop.woowasap.shop.app.spi.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductConnectorService implements ProductConnector {

    private final ProductRepository productRepository;

    @Override
    public Product getByProductId(final long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new CannotFindProductException(
                MessageFormat.format("productId 에 해당하는 Product 가 존재하지 않습니다. productId : \"{0}\"",
                    productId)
            ));
    }
}
