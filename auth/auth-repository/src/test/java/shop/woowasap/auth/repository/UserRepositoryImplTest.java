package shop.woowasap.auth.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import shop.woowasap.BeanScanBaseLocation;
import shop.woowasap.auth.domain.User;
import shop.woowasap.auth.domain.UserType;
import shop.woowasap.auth.repository.jpa.UserJpaRepository;

@Slf4j
@DataJpaTest
@DisplayName("인증 서비스 테스트")
@ContextConfiguration(classes = {BeanScanBaseLocation.class, UserRepositoryImpl.class})
class UserRepositoryImplTest {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Nested
    @DisplayName("UserRepositoryImpl.insertUser 메소드")
    class UserRepositoryImplInsertUserMethod {

        @Test
        @DisplayName("정상 입력 시 유저 생성 성공")
        void insertUserSuccessThenReturnUser() {
            // given
            User user = User.builder()
                .id(123L)
                .username("username")
                .password("hashedPassword")
                .userType(UserType.ROLE_USER)
                .build();

            // when
            User result = userRepository.insertUser(user);

            // then
            assertThat(result).extracting(
                User::getId,
                User::getUsername,
                User::getPassword,
                User::getUserType
            ).containsExactly(
                123L,
                "username",
                "hashedPassword",
                UserType.ROLE_USER
            );
        }
    }

    @Nested
    @DisplayName("UserRepositoryImpl.findByUsername 메소드")
    class UserRepositoryImplFindByUsernameMethod {

        @Test
        @DisplayName("정상 입력 시 유저 반환 성공")
        void findByUsernameSuccessThenReturnOptionalUser() {
            // given
            User user = User.builder()
                .id(123L)
                .username("username")
                .password("hashedPassword")
                .userType(UserType.ROLE_USER)
                .build();
            userRepository.insertUser(user);

            // when
            Optional<User> result = userRepository.findByUsername("username");

            // then
            assertThat(result).isNotEmpty();
            assertThat(result.get()).extracting(
                User::getId,
                User::getUsername,
                User::getPassword,
                User::getUserType
            ).containsExactly(
                123L,
                "username",
                "hashedPassword",
                UserType.ROLE_USER
            );
        }

        @Test
        @DisplayName("없는 유저 입력 시 빈 옵셔널 반환")
        void findByUsernameFailThenReturnOptionalEmpty() {
            // when
            Optional<User> result = userRepository.findByUsername("username");

            // then
            assertThat(result).isEmpty();
        }
    }

}
