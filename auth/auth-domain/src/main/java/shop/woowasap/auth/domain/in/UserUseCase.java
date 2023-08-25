package shop.woowasap.auth.domain.in;

import shop.woowasap.auth.domain.in.request.UserCreateRequest;
import shop.woowasap.auth.domain.in.request.UserLoginRequest;
import shop.woowasap.auth.domain.in.response.LoginResponse;

public interface UserUseCase {

    void createUser(final UserCreateRequest userCreateRequest);

    LoginResponse login(final UserLoginRequest userLoginRequest);

}
