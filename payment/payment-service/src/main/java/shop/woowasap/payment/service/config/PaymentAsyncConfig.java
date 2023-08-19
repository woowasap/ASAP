package shop.woowasap.payment.service.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class PaymentAsyncConfig {

    public static final String STOCK_FAIL = "stockFailEventExecutor";
    public static final String STOCK_SUCCESS = "stockSuccessEventExecutor";


    @Bean(STOCK_FAIL)
    public Executor stockFailEventExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix(STOCK_FAIL);
        executor.initialize();
        return executor;
    }

    @Bean(STOCK_SUCCESS)
    public Executor stockSuccessEventExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix(STOCK_SUCCESS);
        executor.initialize();
        return executor;
    }
}

