package shop.woowasap.core.id.snowflake;


import com.github.f4b6a3.tsid.TsidFactory;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import shop.woowasap.core.id.api.IdGenerator;

@Component
final class SnowflakeIdGenerator implements IdGenerator {

    private static final int CONCURRENT_THREAD_THRESHOLD = 256;
    private static final Map<Long, TsidFactory> SNOWFLAKE_THREAD_FACTORY = new HashMap<>();

    static {
        for (int threadId = 0; threadId < CONCURRENT_THREAD_THRESHOLD; threadId++) {
            SNOWFLAKE_THREAD_FACTORY.put((long) threadId, TsidFactory.newInstance256(threadId));
        }
    }

    @Override
    public long generate() {
        long currentThreadId = Thread.currentThread().getId();
        return SNOWFLAKE_THREAD_FACTORY.get(currentThreadId % CONCURRENT_THREAD_THRESHOLD)
            .create()
            .toLong();
    }
}
