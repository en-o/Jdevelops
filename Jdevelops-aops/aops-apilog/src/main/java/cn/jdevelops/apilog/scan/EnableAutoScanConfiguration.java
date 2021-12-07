package cn.jdevelops.apilog.scan;

import cn.jdevelops.apilog.aspect.ApiLogAspectSave;
import cn.jdevelops.apilog.aspect.ApiLogAspectSee;
import cn.jdevelops.apilog.server.impl.ApiLogSaveImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * 自动扫描
 * @author tn
 * @date 2020-09-27 10:17
 */
@ConditionalOnWebApplication
@Import({ApiLogAspectSee.class,
        ApiLogAspectSave.class,
})
@ComponentScan("cn.jdevelops.apilog.**")
public class EnableAutoScanConfiguration {

    @ConditionalOnMissingBean(name = "apiLogSaveImpl")
    @Bean
    public ApiLogSaveImpl apiLogSaveImpl(){
        return new ApiLogSaveImpl();
    }
}
