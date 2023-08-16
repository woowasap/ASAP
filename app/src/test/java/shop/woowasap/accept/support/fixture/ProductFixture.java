package shop.woowasap.accept.support.fixture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import shop.woowasap.auth.controller.request.LoginRequest;
import shop.woowasap.shop.app.api.request.RegisterProductRequest;
import shop.woowasap.shop.app.api.request.UpdateProductRequest;
import shop.woowasap.shop.app.api.response.ProductResponse;
import shop.woowasap.shop.app.api.response.ProductsResponse;

public class ProductFixture {

    public static final String FORBIDDEN_USERNAME = "forbiddenUsername";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String NAME = "productName";
    public static final String DESCRIPTION = "productDescription";
    public static final String PRICE = "10000";
    public static final long QUANTITY = 10L;
    public static final LocalDateTime START_TIME = LocalDateTime.of(2023, 8, 5, 12, 30);
    public static final LocalDateTime END_TIME = LocalDateTime.of(2023, 8, 5, 14, 30);
    public static final LocalDateTime INFINITE_START_TIME = LocalDateTime.of(2030, 12, 1, 23, 59);
    public static final LocalDateTime INFINITE_END_TIME = LocalDateTime.of(2030, 12, 31, 23, 59);
    public static final long UNKNOWN_ID = 1L;
    public static final int PAGE = 1;
    public static final int TOTAL_PAGE = 1;


    public static LoginRequest loginRequest() {
        return new LoginRequest(USERNAME, PASSWORD);
    }

    public static LoginRequest forbiddenUserLoginRequest() {
        return new LoginRequest(FORBIDDEN_USERNAME, PASSWORD);
    }

    public static RegisterProductRequest registerProductRequest() {
        return new RegisterProductRequest(NAME, DESCRIPTION, PRICE, QUANTITY, START_TIME, END_TIME);
    }

    public static UpdateProductRequest updateProductRequest() {
        return new UpdateProductRequest(NAME, DESCRIPTION, PRICE, QUANTITY, START_TIME, END_TIME);
    }

    public static ProductsResponse productsResponse(final Long productId) {
        return new ProductsResponse(
            List.of(
                new ProductsResponse.ProductResponse(productId, NAME, PRICE, START_TIME, END_TIME)),
            1,
            1
        );
    }

    public static RegisterProductRequest registerValidProductRequest() {
        return new RegisterProductRequest(NAME, DESCRIPTION, PRICE, QUANTITY, INFINITE_START_TIME,
            INFINITE_END_TIME);
    }

    public static RegisterProductRequest registerInvalidProductRequest() {
        return new RegisterProductRequest(NAME, DESCRIPTION, PRICE, QUANTITY,
            LocalDateTime.now().minusHours(3), LocalDateTime.now().minusHours(2));
    }

    public static ProductsResponse productsResponse(
        final List<RegisterProductRequest> registerProductRequests) {
        final List<ProductsResponse.ProductResponse> products = registerProductRequests.stream()
            .map(product -> new ProductsResponse.ProductResponse(
                UNKNOWN_ID,
                product.name(),
                product.price(),
                product.startTime(),
                product.endTime()
            )).collect(Collectors.toList());

        return new ProductsResponse(products, PAGE, TOTAL_PAGE);
    }

    public static ProductResponse productResponse(RegisterProductRequest registerProductRequest) {
        return new ProductResponse(UNKNOWN_ID, registerProductRequest.name(),
            registerProductRequest.description(), registerProductRequest.price(),
            registerProductRequest.quantity(), registerProductRequest.startTime(),
            registerProductRequest.endTime());
    }
}
