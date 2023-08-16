package shop.woowasap.auth.domain.in;

import java.util.List;
import shop.woowasap.auth.domain.in.response.UserResponse;

public interface TokenProvider {

    String generateToken(UserResponse userResponse);

    boolean validateToken(String accessToken);

    List<String> getAuthorities(String accessToken);

    String getUserId(String accessToken);
}
