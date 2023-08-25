package shop.woowasap.auth.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
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
            final User user = User.builder()
                .id(123L)
                .username("username")
                .password("hashedPassword")
                .userType(UserType.ROLE_USER)
                .build();

            // when
            final User result = userRepository.insertUser(user);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(user);
        }
    }

    @Nested
    @DisplayName("UserRepositoryImpl.findByUsername 메소드")
    class UserRepositoryImplFindByUsernameMethod {

        @Test
        @DisplayName("정상 입력 시 유저 반환 성공")
        void findByUsernameSuccessThenReturnOptionalUser() {
            // given
            final User user = User.builder()
                .id(123L)
                .username("username")
                .password("hashedPassword")
                .userType(UserType.ROLE_USER)
                .build();
            userRepository.insertUser(user);

            // when
            final Optional<User> result = userRepository.findByUsername("username");

            // then
            assertThat(result).isNotEmpty();
            assertThat(result.get()).usingRecursiveComparison().isEqualTo(user);
        }

        @Test
        @DisplayName("없는 유저 입력 시 빈 옵셔널 반환")
        void findByUsernameFailThenReturnOptionalEmpty() {
            // when
            final Optional<User> result = userRepository.findByUsername("username");

            // then
            assertThat(result).isEmpty();
        }
    }

}
