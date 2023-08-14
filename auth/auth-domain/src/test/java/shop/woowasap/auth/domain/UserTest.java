package shop.woowasap.auth.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import shop.woowasap.auth.domain.exception.DuplicatedUserIdException;

@DisplayName("유저 객체 테스트")
class UserTest {

    @Nested
    @DisplayName("User.assertNotDuplicatedUserId 메소드")
    class UserAssertNotDuplicatedUserIdMethod {

        @Test
        @DisplayName("유저 아이디 같으면 예외 발생")
        void duplicateThenThrow() {
            // given
            String userId = "helloworld";
            String password = "{bcrypt}encryptedhelloworldpassword";
            User user1 = User.builder().userId(userId).password(password).build();
            User user2 = User.builder().userId(userId).password(password).build();

            // when
            assertThrows(DuplicatedUserIdException.class,
                () -> user1.assertNotDuplicatedUserId(user2));
        }

        @Test
        @DisplayName("유저 아이디 다르면 통과")
        void notDuplicateThenSuccess() {
            // given
            String userId = "helloworld";
            String password = "{bcrypt}encryptedhelloworldpassword";
            User user1 = User.builder().userId(userId).password(password).build();
            User user2 = User.builder().userId(userId + "1").password(password).build();

            // when
            assertDoesNotThrow(() -> user1.assertNotDuplicatedUserId(user2));
        }
    }
}
