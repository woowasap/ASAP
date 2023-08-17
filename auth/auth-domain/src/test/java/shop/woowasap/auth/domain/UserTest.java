package shop.woowasap.auth.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import shop.woowasap.auth.domain.exception.DuplicatedUsernameException;

@DisplayName("유저 객체 테스트")
class UserTest {

    @Nested
    @DisplayName("User.assertNotDuplicatedUsername 메소드")
    class UserAssertNotDuplicatedUsernameMethod {

        @Test
        @DisplayName("유저 아이디 같으면 예외 발생")
        void duplicateThenThrow() {
            // given
            String username = "helloworld";
            String password = "{bcrypt}encryptedhelloworldpassword";
            User user1 = User.builder().username(username).password(password)
                .userType(UserType.ROLE_USER).build();
            User user2 = User.builder().username(username).password(password)
                .userType(UserType.ROLE_USER).build();

            // when
            assertThrows(DuplicatedUsernameException.class,
                () -> user1.assertNotDuplicatedUsername(user2));
        }

        @Test
        @DisplayName("유저 아이디 다르면 통과")
        void notDuplicateThenSuccess() {
            // given
            String username = "helloworld";
            String password = "{bcrypt}encryptedhelloworldpassword";
            User user1 = User.builder().username(username).password(password)
                .userType(UserType.ROLE_USER).build();
            User user2 = User.builder().username(username + "1").password(password)
                .userType(UserType.ROLE_USER).build();

            // when
            assertDoesNotThrow(() -> user1.assertNotDuplicatedUsername(user2));
        }
    }
}
