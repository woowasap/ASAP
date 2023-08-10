package shop.woowasap.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.woowasap.shop.app.api.ProductUseCase;
import shop.woowasap.shop.app.api.response.ProductResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductUseCase productUseCase;

    @GetMapping("/{product-id}")
    public ResponseEntity<ProductResponse> getByProductId(
        @PathVariable("product-id") long productId) {
        
        return ResponseEntity.ok(productUseCase.getById(productId));
    }

}
