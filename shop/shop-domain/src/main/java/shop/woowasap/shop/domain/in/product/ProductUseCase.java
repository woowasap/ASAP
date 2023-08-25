package shop.woowasap.shop.domain.in.product;

import shop.woowasap.shop.domain.in.product.request.RegisterProductRequest;
import shop.woowasap.shop.domain.in.product.request.UpdateProductRequest;
import shop.woowasap.shop.domain.in.product.response.ProductDetailsResponse;
import shop.woowasap.shop.domain.in.product.response.ProductsAdminResponse;
import shop.woowasap.shop.domain.in.product.response.ProductsResponse;

public interface ProductUseCase {

    void update(final long productId, final UpdateProductRequest updateProductRequest);

    Long registerProduct(final RegisterProductRequest registerProductRequest);

    ProductDetailsResponse getByProductId(final long productId);

    ProductDetailsResponse getByProductIdWithAdmin(final long productId);

    ProductsResponse getValidProducts(final String startTime, final Long productId);

    ProductsAdminResponse getProductsInAdmin(final int page, final int size);
}
