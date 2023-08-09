package shop.woowasap.core.id.snowflake;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.waitAtMost;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import shop.woowasap.core.id.api.IdGenerator;

@ExtendWith(SpringExtension.class)
@DisplayName("SnowflakeIdGenerator 클래스")
@ContextConfiguration(classes = SnowflakeIdGenerator.class)
class SnowflakeIdGeneratorTest {

    @Autowired
    private IdGenerator idGenerator;

    @Nested
    @DisplayName("generate 메소드는")
    class generateMethod {

        @Test
        @DisplayName("long id를 생성한다")
        void createLongTypeId() {
            // given
            final Class<Long> expectedType = Long.class;

            // when
            long id = idGenerator.generate();

            // then
            assertThat(id).isInstanceOf(expectedType);
        }

        @Test
        @DisplayName("멀티스레드 환경에서 모두 다른 id가 생성됨을 보장한다")
        void createAllDifferentIdOnMultiThread() {
            // given
            final int expectedGeneratedIdCount = 10000;
            final ExecutorService executorService = Executors.newFixedThreadPool(256);
            final List<Callable<Long>> idGenerateThreads = idGenerateThreads(
                expectedGeneratedIdCount);

            // when & then
            waitAtMost(ofSeconds(10))
                .until(() -> generateId(executorService, idGenerateThreads).size(),
                    Matchers.equalTo(expectedGeneratedIdCount));
        }

        private List<Callable<Long>> idGenerateThreads(int actorCount) {
            final List<Callable<Long>> actors = new ArrayList<>();
            for (int i = 0; i < actorCount; i++) {
                actors.add(() -> idGenerator.generate());
            }
            return actors;
        }

        private Set<Long> generateId(ExecutorService executorService,
            List<Callable<Long>> callableList) throws InterruptedException, ExecutionException {

            final Set<Long> generatedId = new HashSet<>();
            final List<Future<Long>> futureList = executorService.invokeAll(callableList);
            for (Future<Long> future : futureList) {
                generatedId.add(future.get());
            }
            return generatedId;
        }
    }
}
