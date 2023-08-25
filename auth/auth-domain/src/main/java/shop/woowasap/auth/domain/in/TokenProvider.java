package shop.woowasap.auth.domain.in;

import java.util.List;
import java.util.Optional;
import shop.woowasap.auth.domain.in.response.UserResponse;

public interface TokenProvider {

    String generateToken(UserResponse userResponse);

    boolean validateToken(String accessToken);

    Optional<String> extractAccessToken(String bearerToken);

    List<String> getAuthorities(String accessToken);

    String getUserId(String accessToken);
}
