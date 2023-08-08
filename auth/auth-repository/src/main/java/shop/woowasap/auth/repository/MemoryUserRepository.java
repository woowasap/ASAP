package shop.woowasap.auth.repository;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.woowasap.auth.domain.User;
import shop.woowasap.auth.domain.exception.DuplicatedUserIdException;
import shop.woowasap.auth.service.out.UserRepository;

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
        Long generatedId = 1L;
        User generatedUser = User.builder().id(generatedId).userId(user.getUserId())
            .password(user.getPassword()).build();
        users.put(generatedUser.getId(), generatedUser);
        userIdIndexes.put(generatedUser.getUserId(), generatedUser);
        return generatedUser;
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return Optional.ofNullable(userIdIndexes.get(userId));
    }
}
