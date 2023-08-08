package shop.woowasap.auth.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.woowasap.auth.domain.User;
import shop.woowasap.auth.domain.exception.DuplicatedUserIdException;
import shop.woowasap.auth.service.dto.request.UserCreateRequest;
import shop.woowasap.auth.service.out.UserRepository;

@DisplayName("인증 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    UserRepository userRepository;

    @Nested
    @DisplayName("AuthService.createUser 메소드")
    class AuthServiceCreateUserMethod {

        @Test
        @DisplayName("정상 입력 시 성공")
        void createUserSuccess() {
            // given
            UserCreateRequest request = new UserCreateRequest("usersid", "userspassword");
            Mockito.when(userRepository.findByUserId("usersid")).thenReturn(Optional.empty());

            // when, then
            assertThatCode(() -> authService.createUser(request)).doesNotThrowAnyException();

            // then
            Mockito.verify(userRepository, Mockito.times(1)).insertUser(any(User.class));
        }

        @Test
        @DisplayName("유저 아이디 중복 시 실패")
        void duplicatedUserIdThenFail() {
            // given
            UserCreateRequest request = new UserCreateRequest("usersid", "userspassword");
            Mockito.when(userRepository.findByUserId("usersid")).thenReturn(Optional.of(
                User.builder().id(1L).userId("usersid").password("hashedpassword").build()));

            // when, then
            assertThatCode(() -> authService.createUser(request))
                .isInstanceOf(DuplicatedUserIdException.class);
        }
    }
}
