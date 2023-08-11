package shop.woowasap.shop.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.woowasap.shop.app.api.ProductUseCase;
import shop.woowasap.shop.app.api.request.RegisterProductRequest;
import shop.woowasap.shop.app.api.request.UpdateProductRequest;
import shop.woowasap.shop.app.api.response.ProductsResponse;
import shop.woowasap.shop.app.exception.CannotFindProductException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/products")
public class ProductAdminController {

    private final ProductUseCase productUseCase;

    @PutMapping("/{product-id}")
    public ResponseEntity<Void> updateProduct(@PathVariable("product-id") Long productId,
        @RequestBody UpdateProductRequest request) {
        productUseCase.update(productId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> registerProduct(
        @RequestBody final RegisterProductRequest registerProductRequest) {
        final Long registeredProductId = productUseCase.registerProduct(registerProductRequest);

        return ResponseEntity.created(URI.create("/v1/admin/products/" + registeredProductId))
            .build();
    }

    @GetMapping
    public ResponseEntity<ProductsResponse> readProductsInAdmin(
        @RequestParam(defaultValue = "1") final int page,
        @RequestParam(defaultValue = "20") final int size
    ) {
        final ProductsResponse productsResponse = productUseCase.getProductsInAdmin(page, size);

        return ResponseEntity.ok(productsResponse);
    }

    @ExceptionHandler(CannotFindProductException.class)
    public ResponseEntity<Void> handleException() {
        return ResponseEntity.badRequest().build();
    }
}
