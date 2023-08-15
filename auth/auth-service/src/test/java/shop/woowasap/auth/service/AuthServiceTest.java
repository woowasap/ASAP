package shop.woowasap.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import shop.woowasap.auth.domain.User;
import shop.woowasap.auth.domain.UserType;
import shop.woowasap.auth.domain.exception.DuplicatedUsernameException;
import shop.woowasap.auth.domain.exception.LoginFailException;
import shop.woowasap.auth.domain.in.TokenProvider;
import shop.woowasap.auth.domain.in.request.UserCreateRequest;
import shop.woowasap.auth.domain.in.request.UserLoginRequest;
import shop.woowasap.auth.domain.in.response.LoginResponse;
import shop.woowasap.auth.domain.out.UserRepository;
import shop.woowasap.core.id.api.IdGenerator;

@DisplayName("인증 서비스 테스트")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AuthService.class)
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private IdGenerator idGenerator;

    @MockBean
    private TokenProvider tokenProvider;

    @Nested
    @DisplayName("AuthService.createUser 메소드")
    class AuthServiceCreateUserMethod {

        @Test
        @DisplayName("정상 입력 시 성공")
        void createUserSuccess() {
            // given
            final UserCreateRequest request = new UserCreateRequest("username", "userspassword");
            when(userRepository.findByUsername("username")).thenReturn(Optional.empty());
            when(passwordEncoder.encode(anyString())).thenReturn("{bcrypt}cryptedpassword");
            when(idGenerator.generate()).thenReturn(1L);

            // when
            assertThatCode(() -> authService.createUser(request)).doesNotThrowAnyException();

            // then
            Mockito.verify(userRepository, Mockito.times(1)).insertUser(any(User.class));
        }

        @Test
        @DisplayName("유저 아이디 중복 시 실패")
        void duplicatedUsernameThenFail() {
            // given
            final UserCreateRequest request = new UserCreateRequest("username", "userspassword");
            when(userRepository.findByUsername("username")).thenReturn(Optional.of(
                User.builder().id(1L).username("username").password("hashedpassword")
                    .userType(UserType.ROLE_USER).build()));

            // when
            final Exception exception = catchException(() -> authService.createUser(request));

            // then
            assertThat(exception).isInstanceOf(DuplicatedUsernameException.class);
        }
    }

    @Nested
    @DisplayName("AuthSevice.login 메소드")
    class AuthServiceLoginMethod {

        @Test
        @DisplayName("정상 입력 시 LoginResponse 안에 액세스 토큰을 반환")
        void loginSuccessThenReturnLoginResponse() {
            // given
            final String username = "username";
            final String password = "password";
            final String hashedPassword = "hashedPassword";
            final String accessToken = "accessToken";
            final User user = User.builder().id(1L).username(username).password(hashedPassword)
                .userType(UserType.ROLE_USER).build();

            final UserLoginRequest userLoginRequest = new UserLoginRequest(username, password);
            when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(password, hashedPassword)).thenReturn(true);
            when(tokenProvider.createToken(any())).thenReturn(accessToken);

            // when
            final LoginResponse result = authService.login(userLoginRequest);

            // then
            assertThat(result).extracting(LoginResponse::accessToken).isEqualTo(accessToken);
        }

        @Test
        @DisplayName("잘못된 유저아이디일 경우 예외 발생")
        void wrongUsernameLoginFailThenThrow() {
            // given
            final String username = "username";
            final String password = "password";

            final UserLoginRequest userLoginRequest = new UserLoginRequest(username, password);
            when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

            // when
            final Exception exception = catchException(() -> authService.login(userLoginRequest));

            // then
            assertThat(exception).isInstanceOf(LoginFailException.class);
        }

        @Test
        @DisplayName("잘못된 비밀번호일 경우 예외 발생")
        void wrongPasswordLoginFailThenReturnLoginResponse() {
            // given
            final String username = "username";
            final String password = "password";
            final String hashedPassword = "hashedPassword";
            final User user = User.builder().id(1L).username(username).password(hashedPassword)
                .userType(UserType.ROLE_USER).build();

            final UserLoginRequest userLoginRequest = new UserLoginRequest(username, password);
            when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(password, hashedPassword)).thenReturn(false);

            // when
            final Exception exception = catchException(() -> authService.login(userLoginRequest));

            // then
            assertThat(exception).isInstanceOf(LoginFailException.class);
        }
    }
}
