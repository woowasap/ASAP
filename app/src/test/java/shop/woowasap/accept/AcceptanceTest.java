package shop.woowasap.accept;

import static org.mockito.Mockito.when;

import io.restassured.RestAssured;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
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

    @BeforeEach
    public void setPort() {
        RestAssured.port = port;
    }
}
