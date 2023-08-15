package shop.woowasap.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.auth.domain.User;
import shop.woowasap.auth.domain.UserType;
import shop.woowasap.auth.domain.exception.DuplicatedUsernameException;
import shop.woowasap.auth.domain.exception.LoginFailException;
import shop.woowasap.auth.domain.in.TokenProvider;
import shop.woowasap.auth.domain.in.UserUseCase;
import shop.woowasap.auth.domain.in.request.UserCreateRequest;
import shop.woowasap.auth.domain.in.request.UserLoginRequest;
import shop.woowasap.auth.domain.in.response.LoginResponse;
import shop.woowasap.auth.domain.in.response.UserResponse;
import shop.woowasap.auth.domain.out.UserRepository;
import shop.woowasap.core.id.api.IdGenerator;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService implements UserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IdGenerator idGenerator;
    private final TokenProvider tokenProvider;

    @Override
    @Transactional
    public void createUser(final UserCreateRequest userCreateRequest) {
        userRepository.findByUsername(userCreateRequest.username()).ifPresent(user -> {
            throw new DuplicatedUsernameException(userCreateRequest.username());
        });
        final Long generatedId = idGenerator.generate();
        final String encodedPassword = passwordEncoder.encode(userCreateRequest.password());
        final User user = User.builder()
            .id(generatedId)
            .username(userCreateRequest.username())
            .password(encodedPassword)
            .userType(UserType.ROLE_USER)
            .build();
        userRepository.insertUser(user);
    }

    @Override
    public LoginResponse login(final UserLoginRequest userLoginRequest) {
        final User user = validateUsernameAndPassword(userLoginRequest);

        final String accessToken = tokenProvider.createToken(new UserResponse(user.getId(),
            user.getUsername(), user.getUserType().getValue()));
        return new LoginResponse(accessToken);
    }

    private User validateUsernameAndPassword(final UserLoginRequest userLoginRequest) {
        final User user = userRepository.findByUsername(userLoginRequest.username())
            .orElseThrow(LoginFailException::new);
        if (!passwordEncoder.matches(userLoginRequest.password(), user.getPassword())) {
            throw new LoginFailException();
        }
        return user;
    }
}
