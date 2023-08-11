package shop.woowasap.infra.filter;

import shop.woowasap.auth.domain.in.response.UserResponse;

public class JwtTokenProvider {

    public String createAccessToken(UserResponse userResponse) {
        return userResponse.userId();
    }

}
