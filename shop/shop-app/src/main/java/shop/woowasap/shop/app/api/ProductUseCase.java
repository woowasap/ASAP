package shop.woowasap.shop.app.api;

import shop.woowasap.shop.app.api.request.RegisterProductRequest;
import shop.woowasap.shop.app.api.request.UpdateProductRequest;

public interface ProductUseCase {

    void update(final long productId, final UpdateProductRequest updateProductRequest);

    Long registerProduct(final RegisterProductRequest registerProductRequest);
}
