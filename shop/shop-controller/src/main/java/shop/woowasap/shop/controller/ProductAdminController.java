package shop.woowasap.shop.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import shop.woowasap.core.util.web.ErrorTemplate;
import shop.woowasap.shop.domain.exception.ProductException;
import shop.woowasap.shop.domain.in.product.ProductUseCase;
import shop.woowasap.shop.domain.in.product.request.RegisterProductRequest;
import shop.woowasap.shop.domain.in.product.request.UpdateProductRequest;
import shop.woowasap.shop.domain.in.product.response.ProductDetailsResponse;
import shop.woowasap.shop.domain.in.product.response.ProductsAdminResponse;

@Slf4j
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
    public ResponseEntity<ProductsAdminResponse> readProducts(
        @RequestParam(defaultValue = "1") final int page,
        @RequestParam(defaultValue = "20") final int size
    ) {
        final ProductsAdminResponse productsAdminResponse = productUseCase.getProductsInAdmin(page, size);

        return ResponseEntity.ok(productsAdminResponse);
    }

    @GetMapping("/{product-id}")
    public ResponseEntity<ProductDetailsResponse> readProduct(
        @PathVariable("product-id") final Long productId) {
        final ProductDetailsResponse productResponse = productUseCase.getByProductIdWithAdmin(productId);

        return ResponseEntity.ok(productResponse);
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorTemplate> handleException(final ProductException productException) {
        log.info(productException.getMessage());

        return ResponseEntity.badRequest()
            .body(ErrorTemplate.of(productException.getMessage()));
    }
}
