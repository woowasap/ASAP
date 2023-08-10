package shop.woowasap.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.auth.domain.User;
import shop.woowasap.auth.domain.exception.DuplicatedUserIdException;
import shop.woowasap.auth.domain.in.UserCreateUseCase;
import shop.woowasap.auth.domain.in.request.UserCreateRequest;
import shop.woowasap.auth.domain.out.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService implements UserCreateUseCase {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void createUser(UserCreateRequest userCreateRequest) {
        userRepository.findByUserId(userCreateRequest.userId()).ifPresent(user -> {
            throw new DuplicatedUserIdException(userCreateRequest.userId());
        });
        Long generatedId = 1L;
        String digest = "a";
        User user = User.builder()
            .id(generatedId)
            .userId(userCreateRequest.userId())
            .password(digest)
            .build();
        userRepository.insertUser(user);
    }
}
