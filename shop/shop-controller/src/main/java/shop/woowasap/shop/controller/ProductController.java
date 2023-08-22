package shop.woowasap.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.woowasap.core.util.web.ErrorTemplate;
import shop.woowasap.shop.domain.exception.ProductException;
import shop.woowasap.shop.domain.in.product.ProductUseCase;
import shop.woowasap.shop.domain.in.product.response.ProductDetailsResponse;
import shop.woowasap.shop.domain.in.product.response.ProductsResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductUseCase productUseCase;

    @GetMapping("/{product-id}")
    public ResponseEntity<ProductDetailsResponse> getByProductId(
        @PathVariable("product-id") long productId) {

        return ResponseEntity.ok(productUseCase.getByProductId(productId));
    }

    @GetMapping
    public ResponseEntity<ProductsResponse> findValidProduct(
        @RequestParam(defaultValue = "1") final int page,
        @RequestParam(defaultValue = "20") final int size) {
        return ResponseEntity.ok(productUseCase.getValidProducts(page, size));
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorTemplate> handleException(final ProductException productException) {
        log.info(productException.getMessage());

        return ResponseEntity.badRequest()
            .body(ErrorTemplate.of(productException.getMessage()));
    }

}
