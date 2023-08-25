package shop.woowasap.shop.domain.in.product;

import shop.woowasap.shop.domain.in.product.request.RegisterProductRequest;
import shop.woowasap.shop.domain.in.product.request.UpdateProductRequest;
import shop.woowasap.shop.domain.in.product.response.ProductDetailsResponse;
import shop.woowasap.shop.domain.in.product.response.ProductsResponse;
import shop.woowasap.shop.domain.in.product.response.ProductsResponseV2;

public interface ProductUseCase {

    void update(final long productId, final UpdateProductRequest updateProductRequest);

    Long registerProduct(final RegisterProductRequest registerProductRequest);

    ProductDetailsResponse getByProductId(final long productId);

    ProductDetailsResponse getByProductIdWithAdmin(final long productId);

    ProductsResponse getValidProducts(final int page, final int size);

    ProductsResponseV2 getValidProductsV2(final String startTime, final Long productId);

    ProductsResponseV2 getValidProductsV3(final int page, final int size);

    ProductsResponse getProductsInAdmin(final int page, final int size);
}
