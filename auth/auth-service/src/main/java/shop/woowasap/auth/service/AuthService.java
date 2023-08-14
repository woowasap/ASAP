package shop.woowasap.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.auth.domain.User;
import shop.woowasap.auth.domain.exception.DuplicatedUsernameException;
import shop.woowasap.auth.domain.in.UserUseCase;
import shop.woowasap.auth.domain.in.request.UserCreateRequest;
import shop.woowasap.auth.domain.out.UserRepository;
import shop.woowasap.core.id.api.IdGenerator;

@Service
@RequiredArgsConstructor
public class AuthService implements UserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IdGenerator idGenerator;

    @Override
    @Transactional
    public void createUser(final UserCreateRequest userCreateRequest) {
        userRepository.findByUserId(userCreateRequest.username()).ifPresent(user -> {
            throw new DuplicatedUsernameException(userCreateRequest.username());
        });
        final Long generatedId = idGenerator.generate();
        final String encodedPassword = passwordEncoder.encode(userCreateRequest.password());
        final User user = User.builder()
            .id(generatedId)
            .username(userCreateRequest.username())
            .password(encodedPassword)
            .build();
        userRepository.insertUser(user);
    }
}
