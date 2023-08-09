package shop.woowasap.shop.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.woowasap.shop.service.ProductService;
import shop.woowasap.shop.service.dto.RegisterProductRequest;
import shop.woowasap.shop.service.dto.UpdateProductRequest;
import shop.woowasap.shop.service.exception.UpdateProductException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/products")
public class ProductController {

    private final ProductService productService;

    @PutMapping("/{product-id}")
    public ResponseEntity<Void> updateProduct(@PathVariable("product-id") Long productId,
        @RequestBody UpdateProductRequest request) {
        productService.update(productId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> registerProduct(
        @RequestBody final RegisterProductRequest registerProductRequest) {
        final Long registeredProductId = productService.registerProduct(registerProductRequest);

        return ResponseEntity.created(URI.create("/v1/admin/products/" + registeredProductId))
            .build();
    }

    @ExceptionHandler(UpdateProductException.class)
    public ResponseEntity<Void> handleException() {
        return ResponseEntity.badRequest().build();
    }
}
