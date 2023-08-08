package shop.woowasap.auth.service.in;

import shop.woowasap.auth.service.dto.request.UserCreateRequest;

public interface UserCreateUseCase {

    void createUser(UserCreateRequest userCreateRequest);

}
