package shop.woowasap.auth.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import shop.woowasap.auth.domain.exception.DuplicatedUsernameException;
import shop.woowasap.auth.domain.exception.UsernameValidateException;

@DisplayName("유저 아이디 테스트")
class UserNameTest {

    @Nested
    @DisplayName("UserName.of 메소드")
    class UserNameOfMethod {

        @Test
        @DisplayName("유저 아이디 정상 생성 테스트")
        void userIdCreateSuccess() {
            // given
            final String value = "thisisuserid12";

            // when
            final UserName userName = UserName.of(value);

            // then
            assertEquals(userName.getValue(), value);
        }

        @Test
        @DisplayName("유저 아이디 null 생성 실패 테스트")
        void nullUserIdCreateFail() {
            // when
            final UsernameValidateException e = assertThrows(UsernameValidateException.class,
                () -> UserName.of(null));

            // then
            assertEquals("유저의 아이디는 있어야 합니다.", e.getMessage());
        }

        @ParameterizedTest
        @DisplayName("유저 아이디 값 비정상시 생성 실패 테스트")
        @ValueSource(strings = {"   ", "aaaa", "a1234567789b123456789c123456789", "Asdafsdgwer",
            "#@!$%^&asd"})
        void wrongValueUserIdCreateFail(String value) {

            // when
            final UsernameValidateException e = assertThrows(UsernameValidateException.class,
                () -> UserName.of(value));

            // then
            assertEquals("유저의 아이디는 " + 5 + "이상, " + 25 + "자 이하의 소문자와 숫자만 가능합니다.", e.getMessage());
        }
    }

    @Nested
    @DisplayName("UserName.assertNotDuplicated 메소드")
    class UserNameAssertNotDuplicatedMethod {

        @Test
        @DisplayName("유저간 아이디가 같으면 예외 발생")
        void duplicatedUserIdThenThrow() {
            // given
            final String value = "helloworld";

            UserName userName1 = UserName.of(value);
            UserName userName2 = UserName.of(value);

            // when, then
            assertThrows(DuplicatedUsernameException.class,
                () -> userName1.assertNotDuplicated(userName2));
        }

        @Test
        @DisplayName("유저간 아이디가 다르면 예외 발생 안함")
        void notDuplicatedUserIdThenVoid() {
            // given
            final String value = "helloworld";

            UserName userName1 = UserName.of(value);
            UserName userName2 = UserName.of(value + "a");

            // when, then
            assertDoesNotThrow(() -> userName1.assertNotDuplicated(userName2));
        }
    }
}
