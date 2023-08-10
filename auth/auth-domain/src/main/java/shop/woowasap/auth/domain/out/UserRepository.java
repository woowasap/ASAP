package shop.woowasap.auth.domain.out;

import java.util.Optional;
import shop.woowasap.auth.domain.User;

public interface UserRepository {

    User insertUser(User user);

    Optional<User> findByUserId(String userId);
}
