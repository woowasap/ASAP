package shop.woowasap.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.auth.domain.User;
import shop.woowasap.auth.domain.exception.DuplicatedUserIdException;
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
    public void createUser(UserCreateRequest userCreateRequest) {
        userRepository.findByUserId(userCreateRequest.userId()).ifPresent(user -> {
            throw new DuplicatedUserIdException(userCreateRequest.userId());
        });
        Long generatedId = idGenerator.generate();
        String digest = passwordEncoder.encode(userCreateRequest.password());
        User user = User.builder()
            .id(generatedId)
            .userId(userCreateRequest.userId())
            .password(digest)
            .build();
        userRepository.insertUser(user);
    }
}
