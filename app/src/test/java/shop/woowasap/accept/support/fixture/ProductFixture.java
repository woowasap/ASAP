package shop.woowasap.accept.support.fixture;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import shop.woowasap.auth.controller.request.LoginRequest;
import shop.woowasap.shop.domain.in.product.request.RegisterProductRequest;
import shop.woowasap.shop.domain.in.product.request.UpdateProductRequest;
import shop.woowasap.shop.domain.in.product.response.ProductDetailsResponse;
import shop.woowasap.shop.domain.in.product.response.ProductResponse;
import shop.woowasap.shop.domain.in.product.response.ProductsResponse;

public class ProductFixture {

    public static final String FORBIDDEN_USERNAME = "forbiddenUsername";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String NAME = "productName";
    public static final String DESCRIPTION = "productDescription";
    public static final String PRICE = "10000";

    public static final long QUANTITY = 10L;

    public static final LocalDateTime AFTER_SALE_START_TIME = LocalDateTime.of(2022, 8, 5, 12, 30);
    public static final LocalDateTime AFTER_SALE_END_TIME = LocalDateTime.of(2022, 8, 5, 14, 30);

    public static final LocalDateTime BEFORE_SALE_START_TIME = LocalDateTime.of(2023, 8, 2, 0, 0);
    public static final LocalDateTime BEFORE_SALE_END_TIME = LocalDateTime.of(2023, 8, 2, 1, 0);

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
        return new RegisterProductRequest(
            NAME,
            DESCRIPTION,
            PRICE,
            QUANTITY,
            BEFORE_SALE_START_TIME,
            BEFORE_SALE_END_TIME
        );
    }

    public static UpdateProductRequest updateProductRequest(Instant startTime, Instant endTime) {
        return new UpdateProductRequest(
            NAME,
            DESCRIPTION,
            PRICE,
            QUANTITY,
            LocalDateTime.ofInstant(startTime, ZoneId.of("UTC")),
            LocalDateTime.ofInstant(endTime, ZoneId.of("UTC"))
        );
    }

    public static ProductsResponse productsResponse(final Long productId) {
        return new ProductsResponse(
            List.of(new ProductResponse(
                productId,
                NAME,
                PRICE,
                BEFORE_SALE_START_TIME,
                BEFORE_SALE_END_TIME
            )),
            1,
            1
        );
    }

    public static RegisterProductRequest registerValidProductRequest() {
        return new RegisterProductRequest(
            NAME,
            DESCRIPTION,
            PRICE,
            QUANTITY,
            BEFORE_SALE_START_TIME,
            BEFORE_SALE_END_TIME
        );
    }

    public static RegisterProductRequest registerValidProductRequest(Instant startTime,
        Instant endTime) {
        return new RegisterProductRequest(
            NAME,
            DESCRIPTION,
            PRICE,
            QUANTITY,
            LocalDateTime.ofInstant(startTime, ZoneId.of("UTC")),
            LocalDateTime.ofInstant(endTime, ZoneId.of("UTC"))
        );
    }

    public static RegisterProductRequest registerInvalidProductRequest() {
        return new RegisterProductRequest(
            NAME,
            DESCRIPTION,
            PRICE,
            QUANTITY,
            AFTER_SALE_START_TIME,
            AFTER_SALE_END_TIME
        );
    }

    public static ProductsResponse productsResponse(
        final List<RegisterProductRequest> registerProductRequests) {
        final List<ProductResponse> products = registerProductRequests.stream()
            .map(product -> new ProductResponse(
                UNKNOWN_ID,
                product.name(),
                product.price(),
                product.startTime(),
                product.endTime()
            )).collect(Collectors.toList());

        return new ProductsResponse(products, PAGE, TOTAL_PAGE);
    }

    public static ProductDetailsResponse productResponse(long id,
        RegisterProductRequest registerProductRequest) {
        return new ProductDetailsResponse(id, registerProductRequest.name(),
            registerProductRequest.description(), registerProductRequest.price(),
            registerProductRequest.quantity(), registerProductRequest.startTime(),
            registerProductRequest.endTime());
    }
}
