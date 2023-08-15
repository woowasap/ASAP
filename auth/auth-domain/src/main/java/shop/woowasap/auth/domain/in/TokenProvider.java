package shop.woowasap.auth.domain.in;

import shop.woowasap.auth.domain.UserType;
import shop.woowasap.auth.domain.in.response.UserResponse;

public interface TokenProvider {

    String createToken(UserResponse userResponse);

    UserType parseToken(String accessToken);

}
