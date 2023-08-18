package shop.woowasap.shop.service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.shop.domain.exception.NotExistsProductException;
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
        Product product = getProduct(productId);

        product = product.update(
            product.getName().getValue(),
            product.getDescription().getValue(),
            product.getPrice().getValue().toString(),
            product.getQuantity().getValue() - consumedQuantity,
            LocalDateTime.ofInstant(product.getStartTime(), ZoneId.of("UTC")),
            LocalDateTime.ofInstant(product.getEndTime(), ZoneId.of("UTC"))
        );

        productRepository.persist(product);
    }

    private Product getProduct(final long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new NotExistsProductException(
                MessageFormat.format("productId 에 해당하는 Product 가 존재하지 않습니다. productId \"{0}\"",
                    productId)
            ));
    }
}
