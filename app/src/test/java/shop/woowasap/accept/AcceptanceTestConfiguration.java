package shop.woowasap.accept;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import shop.woowasap.auth.domain.User;
import shop.woowasap.auth.domain.UserType;
import shop.woowasap.auth.domain.out.UserRepository;

@TestConfiguration
public class AcceptanceTestConfiguration {

    @Bean
    @Primary
    public Clock fixedClock() {
        return Clock.fixed(Instant.parse("2023-08-15T00:00:02.00Z"), ZoneId.of("Asia/Seoul"));
    }

    @Bean
    public ApplicationRunner dataInitializer(UserRepository userRepository,
        PasswordEncoder passwordEncoder) {
        return args -> {
            User admin = User.builder()
                .id(12345L)
                .username("admin")
                .password(passwordEncoder.encode("1234567890"))
                .userType(UserType.ROLE_ADMIN)
                .build();
            userRepository.insertUser(admin);
        };
    }
}
