package shop.woowasap.shop.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.shop.dto.UpdateProductRequest;

@Service
@Transactional(readOnly = true)
public class ProductService {

    public void update(long productId, UpdateProductRequest updateProductRequest) {

    }
}
