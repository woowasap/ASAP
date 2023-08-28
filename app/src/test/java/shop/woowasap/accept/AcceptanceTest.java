package shop.woowasap.accept;

import io.restassured.RestAssured;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import shop.woowasap.BeanScanBaseLocation;
import shop.woowasap.core.util.time.TimeUtil;

@Import(AcceptanceTestConfiguration.class)
@ContextConfiguration(classes = BeanScanBaseLocation.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
abstract class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    protected TimeUtil timeUtil;

    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    public void setPort() {
        RestAssured.port = port;
        final Set<String> keys = redisTemplate.keys("*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
