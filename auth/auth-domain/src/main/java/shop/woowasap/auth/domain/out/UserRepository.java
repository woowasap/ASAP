package shop.woowasap.auth.domain.out;

import java.util.Optional;
import shop.woowasap.auth.domain.User;

public interface UserRepository {

    User insertUser(final User user);

    Optional<User> findByUserId(final String userId);
}
