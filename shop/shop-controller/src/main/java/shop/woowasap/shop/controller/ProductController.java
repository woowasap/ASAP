package shop.woowasap.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.woowasap.shop.service.ProductService;
import shop.woowasap.shop.service.dto.ProductResponse;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products/details/{product-id}")
    public ProductResponse getProductDetail(@PathVariable("product-id") final long productId) {
        return productService.getProductById(productId);
    }
}
