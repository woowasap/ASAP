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
class UsernameTest {

    @Nested
    @DisplayName("Username.of 메소드")
    class UsernameOfMethod {

        @Test
        @DisplayName("유저 아이디 정상 생성 테스트")
        void usernameCreateSuccess() {
            // given
            final String value = "thisisusername12";

            // when
            final Username username = Username.of(value);

            // then
            assertEquals(username.getValue(), value);
        }

        @Test
        @DisplayName("유저 아이디 null 생성 실패 테스트")
        void nullUsernameCreateFail() {
            // when
            final UsernameValidateException e = assertThrows(UsernameValidateException.class,
                () -> Username.of(null));

            // then
            assertEquals("유저의 아이디는 있어야 합니다.", e.getMessage());
        }

        @ParameterizedTest
        @DisplayName("유저 아이디 값 비정상시 생성 실패 테스트")
        @ValueSource(strings = {"   ", "aaaa", "a1234567789b123456789c123456789", "Asdafsdgwer",
            "#@!$%^&asd"})
        void wrongValueUsernameCreateFail(String value) {

            // when
            final UsernameValidateException e = assertThrows(UsernameValidateException.class,
                () -> Username.of(value));

            // then
            assertEquals("유저의 아이디는 " + 5 + "이상, " + 25 + "자 이하의 소문자와 숫자만 가능합니다.", e.getMessage());
        }
    }

    @Nested
    @DisplayName("Username.assertNotDuplicated 메소드")
    class UsernameAssertNotDuplicatedMethod {

        @Test
        @DisplayName("유저간 아이디가 같으면 예외 발생")
        void duplicatedUsernameThenThrow() {
            // given
            final String value = "helloworld";

            Username username1 = Username.of(value);
            Username username2 = Username.of(value);

            // when, then
            assertThrows(DuplicatedUsernameException.class,
                () -> username1.assertNotDuplicated(username2));
        }

        @Test
        @DisplayName("유저간 아이디가 다르면 예외 발생 안함")
        void notDuplicatedUsernameThenVoid() {
            // given
            final String value = "helloworld";

            Username username1 = Username.of(value);
            Username username2 = Username.of(value + "a");

            // when, then
            assertDoesNotThrow(() -> username1.assertNotDuplicated(username2));
        }
    }
}
