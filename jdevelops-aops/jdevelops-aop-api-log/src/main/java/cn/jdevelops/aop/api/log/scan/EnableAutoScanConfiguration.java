package cn.jdevelops.aop.api.log.scan;

import cn.jdevelops.aop.api.log.aspect.ApiLogAspectSave;
import cn.jdevelops.aop.api.log.server.ApiLogSave;
import cn.jdevelops.aop.api.log.server.impl.ApiLogSaveImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * 自动扫描
 * @author tn
 * @date 2020-09-27 10:17
 */
@AutoConfiguration
@Import({ApiLogAspectSave.class,
})
public class EnableAutoScanConfiguration {

    @ConditionalOnMissingBean(name = {"apiLogSave","apiLogSaveImpl"})
    @Bean
    public ApiLogSave apiLogSaveImpl(){
        return new ApiLogSaveImpl();
    }
}
