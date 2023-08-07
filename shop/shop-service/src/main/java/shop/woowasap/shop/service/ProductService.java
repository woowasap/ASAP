package shop.woowasap.shop.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.shop.domain.Product;
import shop.woowasap.shop.service.dto.ProductResponse;
import shop.woowasap.shop.service.exception.CannotFindProductException;
import shop.woowasap.shop.service.repository.ProductRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    @Value("${zone.id:Asia/Seoul}")
    private String zoneId;

    public ProductResponse getProductById(final long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new CannotFindProductException(productId));

        return new ProductResponse(product.getId(), product.getName(), product.getDescription(),
            product.getPrice().toString(), product.getQuantity(),
            LocalDateTime.ofInstant(product.getStartTime(), ZoneId.of(zoneId)),
            LocalDateTime.ofInstant(product.getEndTime(), ZoneId.of(zoneId)));
    }
}
