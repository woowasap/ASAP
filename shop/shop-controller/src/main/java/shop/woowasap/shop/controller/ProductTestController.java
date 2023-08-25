package shop.woowasap.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.woowasap.core.util.web.ErrorTemplate;
import shop.woowasap.shop.domain.exception.ProductException;
import shop.woowasap.shop.domain.in.product.ProductUseCase;
import shop.woowasap.shop.domain.in.product.response.ProductsResponseV2;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductTestController {

    private final ProductUseCase productUseCase;

    @GetMapping("/v2/products")
    public ResponseEntity<ProductsResponseV2> findValidProductV2(
        @RequestParam(defaultValue = "1980-01-01T00:00:00.000Z") final String startTime,
        @RequestParam(name = "product-id", defaultValue = "0") final Long productId
    ) {
        return ResponseEntity.ok(productUseCase.getValidProductsV2(startTime, productId));
    }

    @GetMapping("/v3/products")
    public ResponseEntity<ProductsResponseV2> findValidProductV3(
        @RequestParam(defaultValue = "1") final int page,
        @RequestParam(defaultValue = "20") final int size
    ) {
        return ResponseEntity.ok(productUseCase.getValidProductsV3(page, size));
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorTemplate> handleException(final ProductException productException) {
        log.info(productException.getMessage());

        return ResponseEntity.badRequest()
            .body(ErrorTemplate.of(productException.getMessage()));
    }
}
