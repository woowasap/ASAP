package shop.woowasap.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.woowasap.shop.service.ProductService;
import shop.woowasap.shop.service.dto.UpdateProductRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductService productService;

    @PutMapping("/{product-id}")
    public ResponseEntity<Void> updateProduct(@PathVariable("product-id") Long productId, UpdateProductRequest request) {
        productService.update(productId, request);
        return ResponseEntity.ok().build();
    }
}
