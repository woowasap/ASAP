package shop.woowasap.accept.product;

import java.time.LocalDateTime;
import shop.woowasap.auth.controller.request.LoginRequest;
import shop.woowasap.shop.domain.in.product.request.RegisterProductRequest;
import shop.woowasap.shop.domain.in.product.request.UpdateProductRequest;

public class ProductFixture {

    public static final String FORBIDDEN_USERNAME = "unauthorizedUsername";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String NAME = "productName";
    public static final String DESCRIPTION = "productDescription";
    public static final String PRICE = "10000";
    public static final int QUANTITY = 10;
    public static final LocalDateTime START_TIME = LocalDateTime.of(2023, 8, 5, 12, 30);
    public static final LocalDateTime END_TIME = LocalDateTime.of(2023, 8, 5, 14, 30);

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
}
