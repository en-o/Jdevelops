package cn.jdevelops.api.log.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 异步配置
 * @author tan
 */
@Configuration
@EnableAsync
public class AsyncConfiguration {


    /**
     * 日志异步 线程池
     */
    @Bean(name = "apiLogAsyncTaskExecutor")
    public Executor apiLogAsyncTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(4);
        taskExecutor.setQueueCapacity(150);
        taskExecutor.setMaxPoolSize(4);
        taskExecutor.setThreadNamePrefix("ApiLogAsyncTaskThread-");
        taskExecutor.initialize();
        return taskExecutor;
    }
}
