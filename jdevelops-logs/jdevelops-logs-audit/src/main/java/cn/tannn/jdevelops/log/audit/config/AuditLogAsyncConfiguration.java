package cn.tannn.jdevelops.log.audit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 异步配置
 * @author tan
 */
@EnableAsync
public class AuditLogAsyncConfiguration {


    /**
     * 日志异步 线程池
     */
    @Bean(name = "auditLogAsyncTaskExecutor")
    public Executor apiLogAsyncTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(4);
        taskExecutor.setQueueCapacity(150);
        taskExecutor.setMaxPoolSize(4);
        taskExecutor.setThreadNamePrefix("AuditLogAsyncTaskExecutor-");
        taskExecutor.initialize();
        return taskExecutor;
    }
}
