package shop.woowasap.auth.repository;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.woowasap.auth.domain.User;
import shop.woowasap.auth.domain.exception.DuplicatedUserIdException;
import shop.woowasap.auth.domain.out.UserRepository;

@Repository
@RequiredArgsConstructor
public class MemoryUserRepository implements UserRepository {

    private static final Map<Long, User> users = new TreeMap<>();
    private static final Map<String, User> userIdIndexes = new TreeMap<>();

    @Override
    public User insertUser(User user) {
        findByUserId(user.getUserId()).ifPresent(u -> {
            throw new DuplicatedUserIdException(user.getUserId());
        });
        users.put(user.getId(), user);
        userIdIndexes.put(user.getUserId(), user);
        return user;
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return Optional.ofNullable(userIdIndexes.get(userId));
    }
}
