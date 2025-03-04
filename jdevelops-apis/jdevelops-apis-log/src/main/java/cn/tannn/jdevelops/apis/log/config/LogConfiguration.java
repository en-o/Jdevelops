package cn.tannn.jdevelops.apis.log.config;


import cn.tannn.jdevelops.apis.log.ApiLogSave;
import cn.tannn.jdevelops.apis.log.DefApiLogSave;
import cn.tannn.jdevelops.apis.log.GlobalApiLogPrint;
import cn.tannn.jdevelops.apis.log.aspect.ApiLogAspectSave;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * 日志处理
 * @author tan
 */
@Import(AsyncConfiguration.class)
public class LogConfiguration {

    /**
     *  DefApiLogSave
     * @return ApiLogSave
     */
    @Bean
    @ConditionalOnMissingBean(ApiLogSave.class)
    @ConditionalOnProperty(
            value="jdevelops.api.log.save.enabled",
            havingValue = "true",
            matchIfMissing = true)
    public ApiLogSave apiLogSave(){
        return new DefApiLogSave();
    }

    @Bean
    @ConditionalOnBean(ApiLogSave.class)
    public ApiLogAspectSave apiLogAspectSave(ApiLogSave apiLogSave){
        return new ApiLogAspectSave(apiLogSave);
    }

    @Bean
    public LogConfig logConfig(){
        return new LogConfig();
    }

    @Bean
    @ConditionalOnProperty(
            value = "jdevelops.api.log.enabled",
            havingValue = "true",
            matchIfMissing = true)
    public GlobalApiLogPrint GlobalApiLogPrint(LogConfig logConfig){
        return new GlobalApiLogPrint(logConfig);
    }
}
