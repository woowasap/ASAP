package shop.woowasap.auth.service.spi;

import java.util.Optional;
import shop.woowasap.auth.domain.User;

public interface UserRepository {

    User insertUser(User user);

    Optional<User> findByUserId(String userId);
}
