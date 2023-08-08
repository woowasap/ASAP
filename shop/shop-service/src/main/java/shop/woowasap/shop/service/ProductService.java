package shop.woowasap.shop.service;

import java.text.MessageFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.shop.domain.Product;
import shop.woowasap.shop.dto.UpdateProductRequest;
import shop.woowasap.shop.service.exception.UpdateProductException;
import shop.woowasap.shop.service.repository.ProductRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public void update(long productId, UpdateProductRequest updateProductRequest) {
        Product product = getProduct(productId);
        Product updateProduct = product.update(productId, updateProductRequest);
        productRepository.save(updateProduct);
    }

    private Product getProduct(long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new UpdateProductException(
                MessageFormat.format("productId 에 해당하는 Product 가 존재하지 않습니다. productId : \"{0}\"",
                    productId)
            ));
    }
}
