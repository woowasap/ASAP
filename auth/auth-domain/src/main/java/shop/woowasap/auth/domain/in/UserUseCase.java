package shop.woowasap.auth.domain.in;

import shop.woowasap.auth.domain.in.request.UserCreateRequest;

public interface UserUseCase {

    void createUser(final UserCreateRequest userCreateRequest);

}
