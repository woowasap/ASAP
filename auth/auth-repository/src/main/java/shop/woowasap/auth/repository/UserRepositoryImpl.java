package shop.woowasap.auth.repository;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.woowasap.auth.domain.User;
import shop.woowasap.auth.domain.out.UserRepository;
import shop.woowasap.auth.repository.entity.UserEntity;
import shop.woowasap.auth.repository.jpa.UserJpaRepository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User insertUser(final User user) {
        final UserEntity userEntity = userJpaRepository.save(UserEntityMapper.toEntity(user));
        return UserEntityMapper.toDomain(userEntity);
    }

    @Override
    public Optional<User> findByUsername(final String username) {
        final Optional<UserEntity> userEntity = userJpaRepository.findByUsername(username);
        return userEntity.map(UserEntityMapper::toDomain);
    }
}
