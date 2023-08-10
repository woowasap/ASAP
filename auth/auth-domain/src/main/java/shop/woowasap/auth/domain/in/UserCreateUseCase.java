package shop.woowasap.auth.domain.in;

import shop.woowasap.auth.domain.in.request.UserCreateRequest;

public interface UserCreateUseCase {

    void createUser(UserCreateRequest userCreateRequest);

}
