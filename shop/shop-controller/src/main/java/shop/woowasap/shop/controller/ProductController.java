package shop.woowasap.shop.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.woowasap.shop.service.ProductService;
import shop.woowasap.shop.service.dto.RegisterProductRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Void> registerProduct(
        @RequestBody final RegisterProductRequest registerProductRequest) {
        final Long registeredProductId = productService.registerProduct(registerProductRequest);

        return ResponseEntity.created(URI.create("/v1/admin/products/" + registeredProductId))
            .build();
    }
}
