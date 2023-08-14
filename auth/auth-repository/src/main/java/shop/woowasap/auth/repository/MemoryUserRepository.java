package shop.woowasap.auth.repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.woowasap.auth.domain.User;
import shop.woowasap.auth.domain.exception.DuplicatedUsernameException;
import shop.woowasap.auth.domain.out.UserRepository;

@Repository
@RequiredArgsConstructor
public class MemoryUserRepository implements UserRepository {

    private final ConcurrentMap<Long, User> users = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, User> usernameIndexes = new ConcurrentHashMap<>();

    @Override
    public User insertUser(final User user) {
        findByUserId(user.getUsername()).ifPresent(u -> {
            throw new DuplicatedUsernameException(user.getUsername());
        });
        users.put(user.getId(), user);
        usernameIndexes.put(user.getUsername(), user);
        return user;
    }

    @Override
    public Optional<User> findByUserId(final String userId) {
        return Optional.ofNullable(usernameIndexes.get(userId));
    }
}
