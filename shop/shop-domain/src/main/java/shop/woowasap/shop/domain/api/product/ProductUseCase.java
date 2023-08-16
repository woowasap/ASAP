package shop.woowasap.shop.domain.api.product;

import shop.woowasap.shop.domain.api.product.request.RegisterProductRequest;
import shop.woowasap.shop.domain.api.product.request.UpdateProductRequest;
import shop.woowasap.shop.domain.api.product.response.ProductDetailsResponse;
import shop.woowasap.shop.domain.api.product.response.ProductsResponse;

public interface ProductUseCase {

    void update(final long productId, final UpdateProductRequest updateProductRequest);

    Long registerProduct(final RegisterProductRequest registerProductRequest);

    ProductDetailsResponse getByProductId(final long productId);

    ProductDetailsResponse getByProductIdWithAdmin(final long productId);

    ProductsResponse getValidProducts(final int page, final int size);

    ProductsResponse getProductsInAdmin(final int page, final int size);
}
