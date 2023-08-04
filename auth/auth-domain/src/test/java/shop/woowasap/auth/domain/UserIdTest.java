package shop.woowasap.auth.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import shop.woowasap.auth.domain.exception.DuplicatedUserIdException;
import shop.woowasap.auth.domain.exception.UserIdValidateException;

@DisplayName("유저 아이디 테스트")
class UserIdTest {

    @DisplayName("유저 아이디 정상 생성 테스트")
    @Test
    void userIdCreateSuccess() {
        // given
        final String value = "thisisuserid12";

        // when
        final UserId userId = UserId.of(value);

        // then
        assertEquals(userId.getValue(), value);
    }

    @DisplayName("유저 아이디 null 생성 실패 테스트")
    @Test
    void nullUserIdCreateFail() {
        // when
        final UserIdValidateException e = assertThrows(UserIdValidateException.class,
            () -> UserId.of(null));

        // then
        assertEquals("유저의 아이디는 있어야 합니다.", e.getMessage());
    }

    @DisplayName("유저 아이디 값 비정상시 생성 실패 테스트")
    @ParameterizedTest
    @ValueSource(strings = {
        "   ",
        "aaaa",
        "a1234567789b123456789c123456789",
        "Asdafsdgwer",
        "#@!$%^&asd"
    })
    void wrongValueUserIdCreateFail(String value) {

        // when
        final UserIdValidateException e = assertThrows(UserIdValidateException.class,
            () -> UserId.of(value));

        // then
        assertEquals("유저의 아이디는 " + 5 + "이상, " + 25 + "자 이하의 소문자와 숫자만 가능합니다.", e.getMessage());
    }

    @DisplayName("유저간 아이디가 같으면 예외 발생")
    @Test
    void duplicatedUserIdThenThrow() {
        // given
        final String value = "helloworld";

        UserId userId1 = UserId.of(value);
        UserId userId2 = UserId.of(value);

        // when, then
        assertThrows(DuplicatedUserIdException.class, () -> userId1.assertNotDuplicated(userId2));
    }

    @DisplayName("유저간 아이디가 다르면 예외 발생 안함")
    @Test
    void notDuplicatedUserIdThenVoid() {
        // given
        final String value = "helloworld";

        UserId userId1 = UserId.of(value);
        UserId userId2 = UserId.of(value + "a");

        // when, then
        assertDoesNotThrow(() -> userId1.assertNotDuplicated(userId2));
    }
}
