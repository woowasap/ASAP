package shop.woowasap.auth.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("유저 객체 테스트")
class UserTest {

    @DisplayName("")
    @Test
    void userCreateSuccess() {
        // given
        String userId = "helloworld";
        String password = "{bcrypt}encryptedhelloworldpassword";

        // when
        User user = assertDoesNotThrow(
            () -> User.builder().userId(userId).password(password).build());

        // then
        assertEquals(userId, user.getUserId());
        assertEquals(password, user.getPassword());
    }
}
