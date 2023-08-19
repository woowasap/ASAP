package shop.woowasap.order.service.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class OrderAsyncConfig {

    public static final String PAY_FAIL = "payFailEventExecutor";
    public static final String PAY_SUCCESS = "paySuccessEventExecutor";


    @Bean(PAY_FAIL)
    public Executor payFailEventExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix(PAY_FAIL);
        executor.initialize();
        return executor;
    }

    @Bean(PAY_SUCCESS)
    public Executor paySuccessEventExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix(PAY_SUCCESS);
        executor.initialize();
        return executor;
    }
}

