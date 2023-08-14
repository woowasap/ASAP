package shop.woowasap.auth.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import shop.woowasap.auth.domain.User;
import shop.woowasap.auth.domain.UserType;
import shop.woowasap.auth.domain.exception.DuplicatedUsernameException;
import shop.woowasap.auth.domain.in.request.UserCreateRequest;
import shop.woowasap.auth.domain.out.UserRepository;
import shop.woowasap.core.id.api.IdGenerator;

@DisplayName("인증 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private IdGenerator idGenerator;

    @Nested
    @DisplayName("AuthService.createUser 메소드")
    class AuthServiceCreateUserMethod {

        @Test
        @DisplayName("정상 입력 시 성공")
        void createUserSuccess() {
            // given
            final UserCreateRequest request = new UserCreateRequest("usersid", "userspassword");
            when(userRepository.findByUsername("usersid")).thenReturn(Optional.empty());
            when(passwordEncoder.encode(anyString())).thenReturn("{bcrypt}cryptedpassword");
            when(idGenerator.generate()).thenReturn(1L);

            // when, then
            assertThatCode(() -> authService.createUser(request)).doesNotThrowAnyException();

            // then
            Mockito.verify(userRepository, Mockito.times(1)).insertUser(any(User.class));
        }

        @Test
        @DisplayName("유저 아이디 중복 시 실패")
        void duplicatedUserIdThenFail() {
            // given
            final UserCreateRequest request = new UserCreateRequest("usersid", "userspassword");
            when(userRepository.findByUsername("usersid")).thenReturn(Optional.of(
                User.builder().id(1L).username("usersid").password("hashedpassword")
                    .userType(UserType.ROLE_USER).build()));

            // when, then
            assertThatCode(() -> authService.createUser(request))
                .isInstanceOf(DuplicatedUsernameException.class);
        }
    }
}
