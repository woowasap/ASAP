package shop.woowasap.accept.support.fixture;

import java.time.LocalDateTime;
import shop.woowasap.mock.dto.ProductResponse;
import shop.woowasap.mock.dto.RegisterProductRequest;

public final class ProductDtoFixture {

    private static final long UNKNOWN_ID = -1;

    private ProductDtoFixture() {
        throw new UnsupportedOperationException(
            "Cannot invoke constructor \"ProductDtoFixture()\"");
    }

    public static RegisterProductRequest registerProductRequest() {
        return new RegisterProductRequest("default-name", "default-description", "1000", 100,
            LocalDateTime.now(), LocalDateTime.now().plusHours(1));
    }

    public static ProductResponse productResponse(RegisterProductRequest registerProductRequest) {
        return new ProductResponse(UNKNOWN_ID, registerProductRequest.name(),
            registerProductRequest.description(), registerProductRequest.price(),
            registerProductRequest.quantity(), registerProductRequest.startTime(),
            registerProductRequest.endTime());
    }

}
