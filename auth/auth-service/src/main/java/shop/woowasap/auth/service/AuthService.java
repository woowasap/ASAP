package shop.woowasap.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.auth.domain.User;
import shop.woowasap.auth.domain.exception.DuplicatedUserIdException;
import shop.woowasap.auth.service.dto.request.UserCreateRequest;
import shop.woowasap.auth.service.in.UserCreateUseCase;
import shop.woowasap.auth.service.out.UserRepository;

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
        User user = userCreateRequest.toDomain();
        userRepository.insertUser(user);
    }
}
