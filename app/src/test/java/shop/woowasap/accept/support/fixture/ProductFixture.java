package shop.woowasap.accept.support.fixture;

import java.time.LocalDateTime;
import shop.woowasap.mock.dto.LoginRequest;
import shop.woowasap.shop.app.api.request.RegisterProductRequest;

public class ProductFixture {

    public static final String FORBIDDEN_USER_ID = "forbiddenUserId";
    public static final String USER_ID = "userId";
    public static final String PASSWORD = "password";
    public static final String NAME = "productName";
    public static final String DESCRIPTION = "productDescription";
    public static final String PRICE = "10000";
    public static final long QUANTITY = 10L;
    public static final LocalDateTime START_TIME = LocalDateTime.of(2023, 8, 5, 12, 30);
    public static final LocalDateTime END_TIME = LocalDateTime.of(2023, 8, 5, 14, 30);

    public static LoginRequest loginRequest() {
        return new LoginRequest(USER_ID, PASSWORD);
    }

    public static LoginRequest forbiddenUserLoginRequest() {
        return new LoginRequest(FORBIDDEN_USER_ID, PASSWORD);
    }

    public static RegisterProductRequest registerProductRequest() {
        return new RegisterProductRequest(NAME, DESCRIPTION, PRICE, QUANTITY, START_TIME, END_TIME);
    }
}
