package shop.woowasap.accept;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import redis.embedded.RedisServer;
import shop.woowasap.auth.domain.User;
import shop.woowasap.auth.domain.UserType;
import shop.woowasap.auth.domain.out.UserRepository;

@TestConfiguration
public class AcceptanceTestConfiguration {

    @Value("${spring.data.redis.port}")
    private int port;

    private RedisServer redisServer;

    @PostConstruct
    public void setUp() throws IOException {
        this.redisServer = new RedisServer(port);
        this.redisServer.start();
    }

    @PreDestroy
    public void stopRedis() throws IOException {
        redisServer.stop();
    }

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
