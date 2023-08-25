package shop.woowasap.accept;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import shop.woowasap.auth.domain.User;
import shop.woowasap.auth.domain.UserType;
import shop.woowasap.auth.domain.out.UserRepository;

@TestConfiguration
public class AcceptanceTestConfiguration {

    @Bean
    public ApplicationRunner dataInitializer(final UserRepository userRepository,
        final PasswordEncoder passwordEncoder) {
        return args -> {
            final User admin = User.builder()
                .id(12345L)
                .username("admin")
                .password(passwordEncoder.encode("1234567890"))
                .userType(UserType.ROLE_ADMIN)
                .build();
            userRepository.insertUser(admin);
        };
    }
}
